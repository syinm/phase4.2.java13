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

@Path("/entwickler")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class EntwicklerController {
    @Inject
    private DataSource dataSource;
    @Context
    private SecurityContext securityContext;
    @Context
    private UriInfo uriInfo;
    //gibt Entwickler zurück
    @GET
    public Response entwickler(@QueryParam("land") String land) {

        try {
            Connection connection = dataSource.getConnection();
            String statement =  "SELECT Entwickler.ROWID,* FROM Entwickler " +
                    "JOIN User ON Entwickler.EMail = User.EMail ";
            if (land != null) statement = statement + "  AND User.Land LIKE '" + land + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;

            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("entwicklerid", resultSet.getObject(1));
                entity.put("studioname", resultSet.getString(4));
                entity.put("homepage", resultSet.getString(3));
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

    //fügt Entwickler hinzu
    @POST
    public Response entwickler(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("land") String land,
                               @FormDataParam("studioname") String studioname,@FormDataParam("homepage") String homepage) {

        if (email == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (passwort == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (land == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (studioname == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (homepage == null) return Response.status(Response.Status.BAD_REQUEST).build();

        try {

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO User (EMail, Passwort, Land) VALUES(?,?,?);");
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.setObject(3, land);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();


            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(("INSERT INTO Entwickler(EMail, Homepage, Studioname)  VALUES (?,?,?)"));
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, homepage);
            preparedStatement.setObject(3, studioname);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();


        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        //TODO richtige URL
        return Response.created(uriInfo.getAbsolutePathBuilder().path("").build()).build();
    }

    // Gibt einen Entwickler zurück
    @GET
    @Path("/{entwicklerid}")
    public Response entwicklerid(@PathParam("entwicklerid") Integer entwicklerid) {

        try {
            Connection connection = dataSource.getConnection();
            String statement = "SELECT Entwickler.ROWID, * FROM Entwickler " +
                    "WHERE  Entwickler.ROWID =" + entwicklerid;


            PreparedStatement preparedStatement = connection.prepareStatement(statement + ";");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;

            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("entwicklerid", resultSet.getObject(1));
                entity.put("studioname", resultSet.getObject(4));
                entity.put("homepage", resultSet.getObject(3));
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