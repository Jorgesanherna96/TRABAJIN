package model;

import notion.api.v1.NotionClient;
import notion.api.v1.http.OkHttp5Client;
import notion.api.v1.logging.Slf4jLogger;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.databases.QueryDatabaseRequest;
import notion.api.v1.request.pages.CreatePageRequest;
import notion.api.v1.request.pages.UpdatePageRequest;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotionRepository implements IRepository {
    private final NotionClient client;
    private final String databaseId;
    private final String titleColumnName = "Identificador"; // Nombre de la columna clave en Notion

    
    public NotionRepository(String apiToken, String databaseId) {
        this.client = new NotionClient(apiToken);
        client.setHttpClient(new OkHttp5Client(60000, 60000, 60000));
        client.setLogger(new Slf4jLogger());
        this.databaseId = databaseId;
    }

    @Override
    public Task addTask(Task t) {
        try {
            if (findPageIdByIdentificador(String.valueOf(t.getIdentificador())) != null) {
                throw new RepositoryException("Ya existe una tarea con el identificador: " + t.getIdentificador());
            }

            Map<String, PageProperty> properties = Map.of(
                "Identificador", createTitleProperty(String.valueOf(t.getIdentificador())),
                "Titulo", createRichTextProperty(t.getTitulo()),
                "Contenido", createRichTextProperty(t.getContenido()),
                "Prioridad", createNumberProperty(t.getPrioridad()),
                "Duracion", createNumberProperty(t.getDuracionEstimada()),
                "Completada", createCheckboxProperty(t.isCompletada()),
                "Fecha", createDateProperty(t.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE))
            );

            PageParent parent = PageParent.database(databaseId);
            CreatePageRequest request = new CreatePageRequest(parent, properties);
            client.createPage(request);
            return t;
        } catch (Exception e) {
            throw new RepositoryException("Error al agregar la tarea: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeTask(Task t) {
        try {
            String pageId = findPageIdByIdentificador(String.valueOf(t.getIdentificador()));
            if (pageId == null) {
                throw new RepositoryException("No se encontró la tarea con el identificador: " + t.getIdentificador());
            }

            UpdatePageRequest request = new UpdatePageRequest(pageId, Collections.emptyMap(), true);
            client.updatePage(request);
        } catch (Exception e) {
            throw new RepositoryException("Error al eliminar la tarea: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifyTask(Task t) {
        try {
            String pageId = findPageIdByIdentificador(String.valueOf(t.getIdentificador()));
            if (pageId == null) {
                throw new RepositoryException("No se encontró la tarea con el identificador: " + t.getIdentificador());
            }

            Map<String, PageProperty> properties = Map.of(
                "Titulo", createRichTextProperty(t.getTitulo()),
                "Contenido", createRichTextProperty(t.getContenido()),
                "Prioridad", createNumberProperty(t.getPrioridad()),
                "Duracion", createNumberProperty(t.getDuracionEstimada()),
                "Completada", createCheckboxProperty(t.isCompletada()),
                "Fecha", createDateProperty(t.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE))
            );

            UpdatePageRequest request = new UpdatePageRequest(pageId, properties);
            client.updatePage(request);
        } catch (Exception e) {
            throw new RepositoryException("Error al modificar la tarea: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);

            for (Page page : queryResults.getResults()) {
                Map<String, PageProperty> properties = page.getProperties();
                Task task = mapPageToTask(properties);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            throw new RepositoryException("Error al obtener las tareas: " + e.getMessage(), e);
        }
        return tasks;
    }

    private String findPageIdByIdentificador(String Identificador) {
        try {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);

            for (Page page : queryResults.getResults()) {
                Map<String, PageProperty> properties = page.getProperties();
                if (properties.containsKey(titleColumnName) &&
                    properties.get(titleColumnName).getTitle().get(0).getText().getContent().equals(Identificador)) {
                    return page.getId();
                }
            }
        } catch (Exception e) {
            throw new RepositoryException("Error al buscar la tarea: " + e.getMessage(), e);
        }
        return null;
    }

    private Task mapPageToTask(Map<String, PageProperty> properties) {
        try {
            int identificador = Integer.parseInt(properties.get("Identificador").getTitle().get(0).getText().getContent());
            String titulo = properties.get("Titulo").getRichText().get(0).getText().getContent();
            String contenido = properties.get("Contenido").getRichText().get(0).getText().getContent();
            int prioridad = properties.get("Prioridad").getNumber().intValue();
            int duracion = properties.get("Duracion").getNumber().intValue();
            boolean completada = properties.get("Completada").getCheckbox();
            String fechaTemporal= properties.get("Fecha").getDate().getStart();
            LocalDate fecha= LocalDate.parse(fechaTemporal);

            return new Task(identificador, titulo,fecha , contenido, prioridad, duracion, completada);
        } catch (Exception e) {
            throw new RepositoryException("Error al mapear las propiedades de Notion a Task: " + e.getMessage(), e);
        }
    }

    private PageProperty createTitleProperty(String title) {
        PageProperty.RichText idText = new PageProperty.RichText();
        idText.setText(new PageProperty.RichText.Text(title));
        PageProperty idProperty = new PageProperty();
        idProperty.setTitle(Collections.singletonList(idText));
        return idProperty;
    }

    private PageProperty createRichTextProperty(String text) {
        PageProperty.RichText richText = new PageProperty.RichText();
        richText.setText(new PageProperty.RichText.Text(text));
        PageProperty property = new PageProperty();
        property.setRichText(Collections.singletonList(richText));
        return property;
    }

    private PageProperty createNumberProperty(Integer number) {
        PageProperty property = new PageProperty();
        property.setNumber(number);
        return property;
    }

    private PageProperty createDateProperty(String date) {
        PageProperty property = new PageProperty();
        PageProperty.Date dateProperty = new PageProperty.Date();
        dateProperty.setStart(date);
        property.setDate(dateProperty);
        return property;
    }

    private PageProperty createCheckboxProperty(boolean checked) {
        PageProperty property = new PageProperty();
        property.setCheckbox(checked);
        return property;
    }
}