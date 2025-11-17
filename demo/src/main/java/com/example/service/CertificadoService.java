package com.example.service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.model.Certificado;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class CertificadoService {
    
    private static final String PASTA_CERTIFICADOS = "certificados";
    
    /**
     * Gera o certificado em PDF
     */
    public String gerarCertificadoPDF(Certificado certificado) throws Exception {
        // Cria a pasta se não existir
        File pasta = new File(PASTA_CERTIFICADOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
            System.out.println("📁 Pasta de certificados criada!");
        }
        
        // Nome do arquivo
        String nomeArquivo = PASTA_CERTIFICADOS + "/certificado_" + 
                            certificado.getNomeAluno().replaceAll(" ", "_") + "_" +
                            certificado.getNomeDisciplina().replaceAll(" ", "_") + "_" +
                            LocalDate.now() + ".pdf";
        
        // Cria o PDF
        PdfWriter writer = new PdfWriter(new FileOutputStream(nomeArquivo));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4.rotate()); // Paisagem
        
        document.setMargins(50, 50, 50, 50);
        
        // ========== BORDA DECORATIVA ==========
        Table bordaExterna = new Table(1);
        bordaExterna.setWidth(UnitValue.createPercentValue(100));
        bordaExterna.setBorder(new com.itextpdf.layout.borders.SolidBorder(
            new DeviceRgb(41, 128, 185), 3));
        
        Table conteudo = new Table(1);
        conteudo.setWidth(UnitValue.createPercentValue(100));
        
        // ========== CABEÇALHO ==========
        Paragraph titulo = new Paragraph("CERTIFICADO DE CONCLUSÃO")
            .setFontSize(32)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(new DeviceRgb(41, 128, 185))
            .setMarginBottom(10);
        
        conteudo.addCell(new Cell().add(titulo).setBorder(Border.NO_BORDER));
        
        // Linha decorativa
        Paragraph linha = new Paragraph("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(new DeviceRgb(149, 165, 166))
            .setMarginBottom(20);
        
        conteudo.addCell(new Cell().add(linha).setBorder(Border.NO_BORDER));
        
        // ========== CORPO DO CERTIFICADO ==========
        Paragraph intro = new Paragraph("Certificamos que")
            .setFontSize(16)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(10);
        
        conteudo.addCell(new Cell().add(intro).setBorder(Border.NO_BORDER));
        
        // Nome do Aluno (destaque)
        Paragraph nomeAluno = new Paragraph(certificado.getNomeAluno())
            .setFontSize(28)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(new DeviceRgb(44, 62, 80))
            .setMarginBottom(15);
        
        conteudo.addCell(new Cell().add(nomeAluno).setBorder(Border.NO_BORDER));
        
        // Texto principal
        String textoStatus = certificado.isAprovado() ? 
            "concluiu com êxito a disciplina" : 
            "participou da disciplina";
        
        Paragraph textoPrincipal = new Paragraph(textoStatus)
            .setFontSize(16)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(10);
        
        conteudo.addCell(new Cell().add(textoPrincipal).setBorder(Border.NO_BORDER));
        
        // Nome da Disciplina (destaque)
        Paragraph nomeDisciplina = new Paragraph(certificado.getNomeDisciplina())
            .setFontSize(24)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setFontColor(new DeviceRgb(52, 152, 219))
            .setMarginBottom(15);
        
        conteudo.addCell(new Cell().add(nomeDisciplina).setBorder(Border.NO_BORDER));
        
        // Turma
        Paragraph turma = new Paragraph("Turma: " + certificado.getNomeTurma())
            .setFontSize(14)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20);
        
        conteudo.addCell(new Cell().add(turma).setBorder(Border.NO_BORDER));
        
        // ========== INFORMAÇÕES ADICIONAIS ==========
        Table tabelaInfo = new Table(2);
        tabelaInfo.setWidth(UnitValue.createPercentValue(60));
        tabelaInfo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        tabelaInfo.setMarginTop(20);
        
        // Média Final
        tabelaInfo.addCell(new Cell().add(new Paragraph("Média Final:").setBold())
            .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(10));
        
        Paragraph mediaValor = new Paragraph(String.format("%.2f", certificado.getMediaFinal()))
            .setBold()
            .setFontColor(certificado.isAprovado() ? 
                new DeviceRgb(39, 174, 96) : new DeviceRgb(231, 76, 60));
        
        tabelaInfo.addCell(new Cell().add(mediaValor)
            .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(10));
        
        // Status
        tabelaInfo.addCell(new Cell().add(new Paragraph("Status:").setBold())
            .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(10));
        
        Paragraph statusValor = new Paragraph(certificado.getStatus())
            .setBold()
            .setFontColor(certificado.isAprovado() ? 
                new DeviceRgb(39, 174, 96) : new DeviceRgb(231, 76, 60));
        
        tabelaInfo.addCell(new Cell().add(statusValor)
            .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(10));
        
        conteudo.addCell(new Cell().add(tabelaInfo).setBorder(Border.NO_BORDER));
        
        // ========== RODAPÉ ==========
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = certificado.getDataEmissao().format(formatter);
        
        Paragraph dataEmissao = new Paragraph("Emitido em: " + dataFormatada)
            .setFontSize(12)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(30)
            .setFontColor(new DeviceRgb(127, 140, 141));
        
        conteudo.addCell(new Cell().add(dataEmissao).setBorder(Border.NO_BORDER));
        
        Paragraph assinatura = new Paragraph("_________________________________\nGestão Escolar - Sistema Acadêmico")
            .setFontSize(12)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginTop(30);
        
        conteudo.addCell(new Cell().add(assinatura).setBorder(Border.NO_BORDER));
        
        // Adiciona conteúdo na borda
        bordaExterna.addCell(new Cell().add(conteudo).setBorder(Border.NO_BORDER).setPadding(30));
        
        document.add(bordaExterna);
        document.close();
        
        System.out.println("✅ Certificado PDF gerado: " + nomeArquivo);
        return nomeArquivo;
    }
}