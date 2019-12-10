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

@Path("/produkte")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class ProdukteController {
    @Inject
    private DataSource dataSource;
    @Context
    private SecurityContext securityContext;
    @Context
    private UriInfo uriInfo;


    @GET
    @Path("/{produktid}")
    public Response produktid(@PathParam("produktid") Integer produktid) {

        try {
            Connection connection = dataSource.getConnection();
            String statement =  "SELECT count(*) FROM (SELECT * FROM Produkt "+
                    "WHERE Produkt.ROWID= " + produktid;

            PreparedStatement preparedStatement = connection.prepareStatement(statement + ");");
            preparedStatement.closeOnCompletion();
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> entities = new ArrayList<>();
            Map<String, Object> entity;
            while (resultSet.next()) {
                entity = new LinkedHashMap<>();
                entity.put("url", resultSet.getObject(1));
                entities.add(entity);
            }
            resultSet.close();
            connection.close();

            return Response.created(uriInfo.getAbsolutePathBuilder().path("").build()).build();


        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}