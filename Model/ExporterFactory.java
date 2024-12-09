package model;

public class ExporterFactory {
    public static IExporter getExporter(String formato) throws ExporterException {

  
        
        try{
        switch (formato.toUpperCase()) {
            case "CSV":
                return new ExportarCSV();
            case "JSON":
                // Regresa un exportador JSON cuando lo implementes
               return new ExportarJSON();
            default:
                throw new  ExporterException("Formato no reconocido: " + formato);
        }
    }catch (Exception e){
        throw new ExporterException("Error al crear Exportador");
    }
}
}