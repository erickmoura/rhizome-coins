package hk.rhizome.coins.resources;

import java.awt.PageAttributes.MediaType;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import hk.rhizome.coins.db.ExchangesDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.Exchanges;
import io.dropwizard.hibernate.UnitOfWork;


@Path("/exchanges")
@Produces(MediaType.APPLICATION_JSON)
public class ExchangesResources {
    
    private ExchangesDAO exchangesDAO;

    public ExchangesResources(ExchangesDAO exchangesDAO){
        this.exchangesDAO = exchangesDAO;
    }

    @GET
    @UnitOfWork
    public List<Exchanges> getExchanges() throws Exception{
        AppLogger.getLogger().debug("Started getExchanges");
        return this.exchangesDAO.findAll();
    }
}