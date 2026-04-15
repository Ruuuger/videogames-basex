package videogames.dao;

import videogames.model.Videojoc;

import java.util.List;

public interface VideojocDAO {
    List<Videojoc> llistarTot() throws Exception;

    Videojoc cercarPerId(String id) throws Exception;

    void afegirVideojoc(Videojoc v) throws Exception;

    void afegirPlataforma(String id, String plataforma) throws Exception;

    void modificarPreu(String id, double nouPreu) throws Exception;

    void modificarEstat(String id, String nouEstat) throws Exception;

    void eliminarVideojoc(String id) throws Exception;
}
