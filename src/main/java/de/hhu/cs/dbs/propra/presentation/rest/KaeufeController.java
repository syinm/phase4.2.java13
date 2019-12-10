package de.hhu.cs.dbs.propra.presentation.rest;

import de.hhu.cs.dbs.propra.domain.model.User;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Path("/kaeufe")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class KaeufeController {
    @Inject
    private DataSource dataSource;
    @Context
    private SecurityContext securityContext;
    @Context
    private UriInfo uriInfo;



// kauft ein Produkt
    @POST
    @RolesAllowed("KUNDE")
    public Response kaufen(@FormDataParam("produktid") Integer produktid, @FormDataParam("gutscheincode") Integer gutscheincode) {

        if (produktid == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (gutscheincode == null) return Response.status(Response.Status.BAD_REQUEST).build();

        try {
            User eingeloggt = (User)(securityContext.getUserPrincipal());

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT EMail FROM Kunde WHERE EMail =" + eingeloggt.getName());
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(("INSERT INTO K_kauft_P(EMail, ProduktID, Gutscheincode) VALUES (?,?,?)"));
            preparedStatement.setObject(1, resultSet.next());
            preparedStatement.setObject(2, produktid);
            preparedStatement.setObject(3, gutscheincode);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(uriInfo.getAbsolutePathBuilder().path("/kaeufe").build()).build();
    }

    // gibt eigene käufe zurück
    @GET
    @RolesAllowed("KUNDE")
    public Response kaeufe(@QueryParam("name") String name) {

        try {
            User eingeloggt = (User) (securityContext.getUserPrincipal());
            Connection connection = dataSource.getConnection();

            String statement = "SELECT K_kauft_P.ROWID,Kunde.ROWID, * FROM  K_kauft_P " +
                    "JOIN Produkt ON K_kauft_P.ProduktID = Produkt.ProduktID " +
                    "JOIN Kunde ON K_kauft_P.EMail = Kunde.EMail " +
                    "WHERE EMail= " + eingeloggt.getName();

            if (name != null) statement = statement + "  AND Produkt.Name LIKE \"%" + name + "%\"";


            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;

            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("kaufid", resultSet.getObject(1));
                entity.put("produktid", resultSet.getObject(4));
                entity.put("name", resultSet.getObject(7));
                entity.put("kundeid", resultSet.getObject(2));
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



    // gibt einen eigenen kauf zurück
    @Path("{kaufid}")
    @GET
    @RolesAllowed("KUNDE")
    public Response einKaeufe(@PathParam("kaufid") Integer kaufid) {

        try {
            Connection connection = dataSource.getConnection();

            String statement = "SELECT K_kauft_P.ROWID,Kunde.ROWID, * FROM  K_kauft_P " +
                    "JOIN Kunde ON K_kauft_P.EMail = Kunde.EMail " +
                    "WHERE K_kauft_P.ROWID= " + kaufid;

            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;


            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("kaufid", resultSet.getObject(1));
                entity.put("gutscheincode", resultSet.getObject(5));
                entity.put("produktid", resultSet.getObject(4));
                entity.put("kundeid", resultSet.getObject(2));
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