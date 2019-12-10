package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.domain.model.User;

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

@Path("/zusatzinhalt")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class ZusatzinhaltController {
    @Inject
    private DataSource dataSource;
    @Context
    private SecurityContext securityContext;
    @Context
    private UriInfo uriInfo;


    @GET
    @Path("/{zusatzinhaltid}")
    public Response zusatzinhalt(@PathParam("zusatzinhaltid") Integer zusatzinhaltid) {

        try {
            Connection connection = dataSource.getConnection();

            String statement =  "SELECT Entwickler.ROWID, Zusatzinhalt.ROWID,Kategorie.ROWID,* FROM Entwickler "+
                                "JOIN Produkt ON Entwickler.EMail = Produkt.EMail " +
                                "JOIN Zusatzinhalt ON Produkt.ProduktID = Zusatzinhalt.ProduktID " +
                                "JOIN Kategorie K on Zusatzinhalt.KBezeichnung = K.Bezeichnung " +
                                "JOIN Kategorie  on Zusatzinhalt.KBezeichnung = Kategorie.Bezeichnung " +
                                "WHERE Zusatzinhalt.ROWID= " + zusatzinhaltid;

            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;

            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("spielid", resultSet.getObject(2));
                entity.put("name", resultSet.getObject(8));
                entity.put("beschreibung", resultSet.getObject(10));
                entity.put("bild", resultSet.getObject(9));
                entity.put("datum", resultSet.getObject(12));
                entity.put("entwicklerid", resultSet.getObject(1));
                entity.put("kategorieid", resultSet.getObject(3));
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
}