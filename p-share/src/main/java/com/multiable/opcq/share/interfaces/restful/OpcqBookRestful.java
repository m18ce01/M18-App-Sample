package com.multiable.opcq.share.interfaces.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.multiable.opcq.share.OpcqStaticVar.OpcqRESTFUL;

@Path(OpcqRESTFUL.book)
public interface OpcqBookRestful {

	@GET
	@Path("checkIsbn")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response wsCheckIsbn(@HeaderParam("isbn") String isbn);

}
