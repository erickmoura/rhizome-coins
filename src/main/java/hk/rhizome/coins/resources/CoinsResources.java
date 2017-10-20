package hk.rhizome.coins.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import hk.rhizome.coins.db.CoinsDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.Coins;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/coins")
@Produces(MediaType.APPLICATION_JSON)
public class CoinsResources{
    
    CoinsDAO coinsDAO;
    public CoinsResources(CoinsDAO coinsDAO){
        this.coinsDAO = coinsDAO;
    }

    @GET
    @Path("/all")
    @UnitOfWork
    public List<Coins> getCoins() throws Exception {
        AppLogger.getLogger().debug("Started get all coins");
        return this.coinsDAO.findAll();
    }
}