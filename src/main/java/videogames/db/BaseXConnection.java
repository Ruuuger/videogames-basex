package videogames.db;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Open;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BaseXConnection {
    private static final String DB_NAME = "cataleg";
    private final Context context;

    public BaseXConnection() throws Exception {
        this.context = new Context();
        inicialitzarBaseDeDades();
    }

    public Context getContext() {
        return context;
    }

    private void inicialitzarBaseDeDades() throws Exception {
        try {
            new Open(DB_NAME).execute(context);
            new Close().execute(context);
        } catch (BaseXException e) {
            URL resource = getClass().getClassLoader().getResource("cataleg.xml");
            if (resource == null) {
                throw new IllegalStateException("No s'ha trobat el fitxer cataleg.xml a resources.");
            }
            String xmlPath;
            if ("jar".equalsIgnoreCase(resource.getProtocol())) {
                Path tempXml = Files.createTempFile("cataleg-", ".xml");
                tempXml.toFile().deleteOnExit();
                try (var inputStream = getClass().getClassLoader().getResourceAsStream("cataleg.xml")) {
                    if (inputStream == null) {
                        throw new IllegalStateException("No s'ha pogut obrir el fitxer cataleg.xml.");
                    }
                    Files.copy(inputStream, tempXml, StandardCopyOption.REPLACE_EXISTING);
                }
                xmlPath = tempXml.toAbsolutePath().toString();
            } else {
                xmlPath = URLDecoder.decode(resource.getPath(), StandardCharsets.UTF_8);
            }
            new CreateDB(DB_NAME, xmlPath).execute(context);
            new Close().execute(context);
        }
    }

    public void close() {
        context.close();
    }
}
