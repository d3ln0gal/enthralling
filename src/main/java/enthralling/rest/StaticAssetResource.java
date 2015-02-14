package enthralling.rest;

import java.io.File;
import java.io.IOException;

import javax.inject.*;

import com.google.common.io.Files;

import restx.RestxContext;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatcher;
import restx.StdRoute;
import restx.factory.Component;
import restx.http.HTTP;

@Component
public class StaticAssetResource extends StdRoute{
	

	@Inject
	public StaticAssetResource(){
        super("static", new StdRestxRequestMatcher("GET", "/static/{file:(.)*$}" ));
	}
	
	@Override
	public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx) throws IOException {
		File file=new File("src/main/webapp/static", match.getPathParam("file"));
		System.out.println(System.getProperty("static.root"));
		System.out.println("FILE____________________" + file.getAbsolutePath());
		if(file.exists()){
			resp.setContentType(HTTP.getContentTypeFromExtension(file.toString()).or("application/octet-stream"));
			Files.copy(file, resp.getOutputStream());
		}else{
			notFound(match, resp);
		}

	}
	
	

}
