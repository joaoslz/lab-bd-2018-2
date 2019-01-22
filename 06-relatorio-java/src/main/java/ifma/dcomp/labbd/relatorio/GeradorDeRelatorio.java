package ifma.dcomp.labbd.relatorio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GeradorDeRelatorio {

    public static void main(String[] args) throws SQLException, JRException, FileNotFoundException, ClassNotFoundException, ParseException {

        Class.forName("com.mysql.jdbc.Driver");

         Connection conexao =
                 DriverManager
                 .getConnection("jdbc:mysql://localhost/financas?useSSL=false",
                               "root",
                            "root");


        Date dataInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2012");
        Date dataFim    = new SimpleDateFormat("dd/MM/yyyy").parse("01/10/2012");


        Map<String, Object> parametros = new HashMap();

        parametros.put("DATA_INICIO", dataInicio );
        parametros.put("DATA_FIM", dataFim );

        JasperCompileManager.compileReport("movimentacoes.jrxml");


        JasperPrint print = JasperFillManager
                             .fillReport("movimentacoes.jasper",
                                          parametros,
                                          conexao );

        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(
                 new SimpleOutputStreamExporterOutput(
                         new FileOutputStream("movimentacoes.pdf" )) );
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        conexao.close();


    }
}
