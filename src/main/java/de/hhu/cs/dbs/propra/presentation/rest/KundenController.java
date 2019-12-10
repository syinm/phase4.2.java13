package de.hhu.cs.dbs.propra.presentation.rest;


import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Path("/kunden")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class KundenController {
    @Inject
    private DataSource dataSource;
    @Context
    private SecurityContext securityContext;
    @Context
    private UriInfo uriInfo;

    //fügt einen Kunden hinzu
    @POST
    public Response kunden(@FormDataParam("email") String email, @FormDataParam("passwort") String passwort, @FormDataParam("land") String land, @FormDataParam("vorname") String vorname,
                            @FormDataParam("nachname") String nachname, @FormDataParam("benutzername") String benutzername) {

        if (email == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (passwort == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (land == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (vorname == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (nachname == null) return Response.status(Response.Status.BAD_REQUEST).build();
        if (benutzername == null) return Response.status(Response.Status.BAD_REQUEST).build();

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO User (EMail, Passwort, Land) VALUES(?,?,?);");
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, passwort);
            preparedStatement.setObject(3, land);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(("INSERT INTO Kunde(EMail, Benutzername, Nachname, Vorname) VALUES (?,?,?,?)"));
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, benutzername);
            preparedStatement.setObject(3, nachname);
            preparedStatement.setObject(4, vorname);
            preparedStatement.closeOnCompletion();
            preparedStatement.execute();

        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(uriInfo.getAbsolutePathBuilder().path("").build()).build();
    }
}