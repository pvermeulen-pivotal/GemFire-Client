package util.geode.demo.client;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.apache.log4j.Logger;

import util.geode.demo.dao.RegionDao;
import util.geode.demo.domain.Domain;

public class DemoApplication {

	private ClientCache cache;
	private Region region1;
	private Region region2;
	private Logger LOG = Logger.getLogger(DemoApplication.class);
	private RegionDao region1Dao;
	private RegionDao region2Dao;

	public DemoApplication() {
	}
	
	public void createGemFireClientRegions() {
		region1 = cache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("Region1");
		region2 = cache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("Region2");
	}

	public void createGemFireClient() {
		LOG.info("Starting client demo " + new Date());
		cache = new ClientCacheFactory().addPoolLocator("localhost", 10334)
				.setPoolMaxConnections(10).setPoolMinConnections(1).setPoolRetryAttempts(-1)
				.setPoolPRSingleHopEnabled(true)
				.setPoolReadTimeout(10000)
				.setPdxSerializer(new ReflectionBasedAutoSerializer("util.geode.demo.domain.Domain"))
				.set("log-level", "CONFIG").set("log-file", "logs/performance-gemfire.log").create();
	}

	public void createRegionDao() {
		region1Dao = new RegionDao(region1);
		region2Dao = new RegionDao(region1);
	}
	
	public void process() {
		region1Dao.save(new Domain("Key1", 1,  120));
		List<Domain> regionObjects = region1Dao.findAll();
		Optional<Domain> domain = region1Dao.findById("key1");
		region2Dao.save(new Domain("Key2", 2,  120));
		regionObjects = region1Dao.findAll();
		domain = region2Dao.findById("key2");
		region2Dao.existsById("Key2");
	}
	
	static public void main(String[] args) throws Exception {
		DemoApplication demo = new DemoApplication();
		demo.createGemFireClient();
		demo.createGemFireClientRegions();
		demo.createRegionDao();
	}

}