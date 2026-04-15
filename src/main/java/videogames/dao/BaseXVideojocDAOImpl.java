package videogames.dao;

import org.basex.api.client.LocalQuery;
import org.basex.api.client.LocalSession;
import org.basex.core.Context;
import org.basex.core.cmd.XQuery;
import videogames.model.Videojoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BaseXVideojocDAOImpl implements VideojocDAO {
    private final Context context;

    public BaseXVideojocDAOImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Videojoc> llistarTot() throws Exception {
        String xquery = "for $j in doc(\"cataleg\")/cataleg/joc "
                + "return concat($j/@id, \"|\", $j/@estat, \"|\", $j/titol, \"|\", $j/desenvolupador, \"|\", $j/preu, \"|\", string-join($j/plataformes/plataforma, \",\"), \"|\", $j/any_llancament)";

        List<Videojoc> videojocs = new ArrayList<>();
        try (LocalSession session = new LocalSession(context);
             LocalQuery query = session.query(xquery)) {
            while (query.more()) {
                videojocs.add(parseLine(query.next()));
            }
        }
        return videojocs;
    }

    @Override
    public Videojoc cercarPerId(String id) throws Exception {
        String xquery = "declare variable $id as xs:string external; "
                + "for $j in doc(\"cataleg\")/cataleg/joc[@id = $id] "
                + "return concat($j/@id, \"|\", $j/@estat, \"|\", $j/titol, \"|\", $j/desenvolupador, \"|\", $j/preu, \"|\", string-join($j/plataformes/plataforma, \",\"), \"|\", $j/any_llancament)";

        try (LocalSession session = new LocalSession(context);
             LocalQuery query = session.query(xquery)) {
            query.bind("id", id);
            if (query.more()) {
                return parseLine(query.next());
            }
            return null;
        }
    }

    @Override
    public void afegirVideojoc(Videojoc v) throws Exception {
        String plataformesXml = v.getPlataformes().stream()
                .map(this::escapeXml)
                .map(p -> "<plataforma>" + p + "</plataforma>")
                .collect(Collectors.joining());

        String jocXml = "<joc id=\"" + escapeXml(v.getId()) + "\" estat=\"" + escapeXml(v.getEstat()) + "\">"
                + "<titol>" + escapeXml(v.getTitol()) + "</titol>"
                + "<desenvolupador>" + escapeXml(v.getDesenvolupador()) + "</desenvolupador>"
                + "<preu>" + String.format(Locale.ROOT, "%.2f", v.getPreu()) + "</preu>"
                + "<plataformes>" + plataformesXml + "</plataformes>"
                + "<any_llancament>" + v.getAnyLlancament() + "</any_llancament>"
                + "</joc>";

        String xquery = "insert node " + jocXml + " into doc(\"cataleg\")/cataleg";
        try (LocalSession session = new LocalSession(context)) {
            session.execute(new XQuery(xquery));
        }
    }

    @Override
    public void afegirPlataforma(String id, String plataforma) throws Exception {
        String xquery = "declare variable $id as xs:string external; "
                + "declare variable $plataforma as xs:string external; "
                + "insert node <plataforma>{$plataforma}</plataforma> "
                + "into doc(\"cataleg\")/cataleg/joc[@id=$id]/plataformes";
        try (LocalSession session = new LocalSession(context);
             LocalQuery query = session.query(xquery)) {
            query.bind("id", id);
            query.bind("plataforma", plataforma);
            query.execute();
        }
    }

    @Override
    public void modificarPreu(String id, double nouPreu) throws Exception {
        String xquery = "declare variable $id as xs:string external; "
                + "declare variable $nouPreu as xs:double external; "
                + "replace value of node doc(\"cataleg\")/cataleg/joc[@id=$id]/preu with $nouPreu";
        try (LocalSession session = new LocalSession(context);
             LocalQuery query = session.query(xquery)) {
            query.bind("id", id);
            query.bind("nouPreu", String.format(Locale.ROOT, "%.2f", nouPreu));
            query.execute();
        }
    }

    @Override
    public void modificarEstat(String id, String nouEstat) throws Exception {
        String xquery = "declare variable $id as xs:string external; "
                + "declare variable $nouEstat as xs:string external; "
                + "replace value of node doc(\"cataleg\")/cataleg/joc[@id=$id]/@estat with $nouEstat";
        try (LocalSession session = new LocalSession(context);
             LocalQuery query = session.query(xquery)) {
            query.bind("id", id);
            query.bind("nouEstat", nouEstat);
            query.execute();
        }
    }

    @Override
    public void eliminarVideojoc(String id) throws Exception {
        String xquery = "declare variable $id as xs:string external; "
                + "delete node doc(\"cataleg\")/cataleg/joc[@id=$id]";
        try (LocalSession session = new LocalSession(context);
             LocalQuery query = session.query(xquery)) {
            query.bind("id", id);
            query.execute();
        }
    }

    private Videojoc parseLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) {
            throw new IllegalArgumentException("Resultat invàlid del catàleg: " + line);
        }
        List<String> plataformes = parts[5].isBlank() ? new ArrayList<>() : Arrays.stream(parts[5].split(","))
                .map(String::trim)
                .filter(p -> !p.isEmpty())
                .collect(Collectors.toList());

        return new Videojoc(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                Double.parseDouble(parts[4]),
                plataformes,
                Integer.parseInt(parts[6])
        );
    }

    private String escapeXml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
