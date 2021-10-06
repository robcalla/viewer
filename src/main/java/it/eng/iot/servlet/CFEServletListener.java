package it.eng.iot.servlet;

import it.eng.digitalenabler.fiware.keyrock7.Keyrock7;
import it.eng.digitalenabler.idm.fiware.IdentityManager;
import it.eng.digitalenabler.restclient.DefaultRestClient;
import it.eng.digitalenabler.sdk.databinder.DefaultDataBinder;
import it.eng.digitalenabler.sdk.databinder.model.DataBinder;
import it.eng.iot.configuration.Conf;
import it.eng.iot.configuration.ConfIDM;
import it.eng.iot.utils.IdentityManagerUtility;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CFEServletListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String filePath = Conf.getInstance().getString("ImageUpload.path");
		String destinationScope = Conf.getInstance().getString("ImageUpload.destinationScope");	
		System.out.println("Starting City Front end");
		if("filesystem".equals(destinationScope)) { 
			new File(filePath).mkdirs();
		}
		
		if("true".equalsIgnoreCase(ConfIDM.getInstance().getString("idm.fiware.enabled"))) {
		
			IdentityManager.setBaseUrl(ConfIDM.getInstance().getString("idm.be.host"));
			IdentityManager.setAdminCredentials(ConfIDM.getInstance().getString("idm.admin.email"), 
												ConfIDM.getInstance().getString("idm.admin.password"));
			IdentityManager.setRestClient(new DefaultRestClient());
			DataBinder dataBinder = new DefaultDataBinder();
							  dataBinder.init();
							  
			IdentityManager.setJsonSerde(dataBinder);
			IdentityManagerUtility.setIdentityManager(new Keyrock7());
		}
	}

}
