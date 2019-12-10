package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.domain.model.User;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/spiele")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class SpieleController {
    @Inject
    private DataSource dataSource;
    @Context
    private SecurityContext securityContext;
    @Context
    private UriInfo uriInfo;

    // Gibt Spiele zurück
    @GET
    public Response alle_spiele(@QueryParam("name") String name, @QueryParam("fsk") Integer fsk, @QueryParam("top") Integer top, @QueryParam("entwicklerid") Integer entwicklerid) {

        try {
            Connection connection = dataSource.getConnection();

            String statement = "SELECT Entwickler.ROWID, Spiel.ROWID,avg(Note),* FROM Spiel " +
                    "JOIN Produkt ON Spiel.ProduktID= Produkt.ProduktID " +
                    "JOIN Entwickler ON Produkt.Email = Entwickler.Email ";

            if (top == null)
                statement = statement + "LEFT JOIN K_bewertet_P on Produkt.ProduktID = K_bewertet_P.ProduktID";
            else statement = statement + "JOIN K_bewertet_P on Produkt.ProduktID = K_bewertet_P.ProduktID";
            if (name != null || fsk != null || top != null || entwicklerid != null)
                statement = statement + " WHERE 1=1 ";
            if (name != null) statement = statement + "  AND Produkt.Name LIKE \"%" + name + "%\"";
            if (fsk != null) statement = statement + " AND FSK = " + fsk;
            if (entwicklerid != null) statement = statement + " AND Entwickler.ROWID =" + entwicklerid;
            statement = statement + " GROUP BY Spiel.ProduktID";
            if (top != null) statement = statement + " ORDER BY avg(Note) LIMIT " + top;

            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;


            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("spielid", resultSet.getObject(2));
                entity.put("name", resultSet.getObject(7));
                entity.put("fsk", resultSet.getObject(5));
                entity.put("durchschnittsbewertung", resultSet.getObject(3));
                entity.put("entwicklerid", resultSet.getObject(1));
                entity.put("produktid", resultSet.getObject(4));
                entities.add(entity);
            }
            resultSet.close();
            connection.close();
            return Response.status(Response.Status.OK).entity(entities).build();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }


    // Gibt ein Spiel zurück
    @GET
    @Path("/{spielid}")
    public Response spieleid(@PathParam("spielid") Integer spielid) {

        try {
            Connection connection = dataSource.getConnection();
            String statement = "SELECT *,Entwickler.ROWID FROM Spiel " +
                    "JOIN Produkt ON Spiel.ProduktID = Produkt.ProduktID " +
                    "LEFT JOIN S_Nachfolger_S SNS on Spiel.ProduktID = SNS.ProduktIDZwei " +
                    "LEFT JOIN Zusatzinhalt ON Spiel.ProduktID = Zusatzinhalt.SpielID " +
                    "JOIN Entwickler ON Produkt.EMail = Entwickler.EMail " +
                    "WHERE Spiel.ROWID= " + spielid;

            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;

            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("spielid", resultSet.getObject(1));
                entity.put("name", resultSet.getObject(4));
                entity.put("beschreibung", resultSet.getObject(6));
                entity.put("bild", resultSet.getObject(5));
                entity.put("datum", resultSet.getObject(8));
                entity.put("fsk", resultSet.getObject(2));
                entity.put("vorgaengerid", resultSet.getObject(9));
                entity.put("zusatzinhalte", resultSet.getObject(11));
                entity.put("entwickelerid", resultSet.getObject(18));
                entity.put("produktid", resultSet.getObject(3));
                entities.add(entity);
            }
            resultSet.close();
            connection.close();
            return Response.status(Response.Status.OK).entity(entities).build();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    // Gibt Zusatzinhalte eines Spiel zurück
    @GET
    @Path("/{spielid}/zusatzinhalte")
    public Response spieleidZusatzinhalte(@PathParam("spielid") Integer spielid, @QueryParam("name") String name, @QueryParam("top") Integer top, @QueryParam("entwicklerid") Integer entwicklerid,
                                          @QueryParam("kategorieid") Integer kategorieid) {

        try {
            Connection connection = dataSource.getConnection();
            String statement = "SELECT Zusatzinhalt.ROWID,Kategorie.ROWID,avg(Note), * FROM Spiel " +
                    "JOIN Zusatzinhalt ON Spiel.ProduktID = Zusatzinhalt.SpielID " +
                    "JOIN Produkt ON Zusatzinhalt.ProduktID = Produkt.ProduktID " +
                    "JOIN Kategorie ON Zusatzinhalt.KBezeichnung = Kategorie.Bezeichnung " +
                    "JOIN Entwickler ON Produkt.EMail = Entwickler.EMail ";

            if (top == null)
                statement = statement + " LEFT JOIN K_bewertet_P ON Zusatzinhalt.ProduktID = K_bewertet_P.ProduktID WHERE  Spiel.ROWID =" + spielid;
            else
                statement = statement + " JOIN K_bewertet_P ON Zusatzinhalt.ProduktID = K_bewertet_P.ProduktID WHERE  Spiel.ROWID =" + spielid;
            if (name != null) statement = statement + "  AND Produkt.Name LIKE \"%" + name + "%\"";
            if (entwicklerid != null) statement = statement + " AND Entwickler.ROWID =" + entwicklerid;
            if (kategorieid != null) statement = statement + " AND Kategorie.ROWID =" + kategorieid;
            statement = statement + " GROUP BY Zusatzinhalt.ROWID ";
            if (top != null) statement = statement + " ORDER BY avg(Note) LIMIT " + top;

            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;

            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("zusatzinhaltid", resultSet.getObject(1));
                entity.put("name", resultSet.getObject(11));
                entity.put("kategorieid", resultSet.getObject(2));
                entity.put("durchschnittsbewertung", resultSet.getObject(3));
                entity.put("produktid", resultSet.getObject(10));

                entities.add(entity);
            }
            resultSet.close();
            connection.close();
            return Response.status(Response.Status.OK).entity(entities).build();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }


    // Fügt spiel hinzu
    @RolesAllowed("{ENTWICKLER}")
    @POST
    public Response addSpiel(@FormDataParam("name") String name, @FormDataParam("beschreibung") String beschreibung, @FormDataParam("bild") String bild,
                             @FormDataParam("datum") String datum, @FormDataParam("fsk") String fsk, @FormDataParam("vorgaengerid") Integer vorgaengerid,
                             @FormDataParam("zusatzinhalte") Integer zusatzinhalte, @FormDataParam("entwicklerid") Integer entwicklerid) {

        if (name == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (beschreibung == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (bild == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (datum == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (fsk == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (vorgaengerid == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (zusatzinhalte == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (entwicklerid == null) return Response.status(Response.Status.BAD_REQUEST).build();


        try {
            User eingeloggt = (User) (securityContext.getUserPrincipal());

            /*
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT EMail FROM Entwickler WHERE Entwickler.ROWID = "+ entwicklerid );
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            */

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Produkt (Name, Bild, Beschreibung, EMail, Datum) VALUES (?,?,?,?,?)");
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, bild);
            preparedStatement.setObject(3, beschreibung);
            preparedStatement.setObject(3, eingeloggt.getName());
            preparedStatement.setObject(3, datum);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();


            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT ProduktID FROM Produkt WHERE Name = " + name);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();


            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO Spiel (ProduktID, FSK) VALUES (?,?)");
            preparedStatement.setObject(1, resultSet.next());
            preparedStatement.setObject(2, fsk);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();


        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(uriInfo.getAbsolutePathBuilder().path("/spiele").build()).build();
    }

    // Fügt Zusatzinhalt hinzu
    @Path("/{spielid}/zusatzinhalte")
    @RolesAllowed("{ENTWICKLER}")
    @POST
    public Response addZusatzinhalt(@QueryParam("spielid") Integer spielid, @FormDataParam("name") String name, @FormDataParam("beschreibung")String beschreibung,
                                    @FormDataParam("bild") String bild, @FormDataParam("datum") String datum, @FormDataParam("entwicklerid") Integer entwicklerid,
                                    @FormDataParam("kategorieid") Integer kategorieid) {

        if (spielid == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (name == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (beschreibung == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (bild == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (datum == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (entwicklerid == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (kategorieid == null) return Response.status(Response.Status.BAD_REQUEST).build();



        try {
            User eingeloggt = (User) (securityContext.getUserPrincipal());

            /*
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT EMail FROM Entwickler WHERE Entwickler.ROWID = "+ entwicklerid );
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            */

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Produkt (Name, Bild, Beschreibung, EMail, Datum) VALUES (?,?,?,?,?)");
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, bild);
            preparedStatement.setObject(3, beschreibung);
            preparedStatement.setObject(3, eingeloggt.getName());
            preparedStatement.setObject(3, datum);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();


            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT ProduktID FROM Produkt WHERE Name = " + name);
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();


            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO Spiel (ProduktID, FSK) VALUES (?,?)");
            preparedStatement.setObject(1, resultSet.next());
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();


        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(uriInfo.getAbsolutePathBuilder().path("/{spielid}/zusatzinhalte").build()).build();
    }


}