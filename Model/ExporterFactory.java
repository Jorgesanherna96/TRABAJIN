package model;

public class ExporterFactory {
    public static IExporter getExporter(String formato) throws ExporterException {

  
        
        try{
        switch (formato.toUpperCase()) {
            case "CSV":
                return new ExportarCSV();
            case "JSON":
               
               return new ExportarJSON();
            default:
                throw new  ExporterException("Formato no reconocido: " + formato);
        }
    }catch (Exception e){
        throw new ExporterException("Error al crear Exportador");
    }
}
}