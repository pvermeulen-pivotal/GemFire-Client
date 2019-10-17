package util.geode.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.geode.cache.Region;

import util.geode.demo.domain.Domain;

public class RegionDao {

	private Region region;
	
	public RegionDao(Region region) {
		this.region=region;
	}
	
	public void save(Domain domain) {
		region.put(domain.getKeyHeader(), domain);
	}

	public void saveAll(Iterable<Domain> entities) {
		region.putAll((Map<? extends String, ? extends Domain >) entities);
	}

	public Optional<Domain> findById(String id) {
		Domain domain = (Domain) region.get(id);
		return Optional.ofNullable(domain);
	}

	public boolean existsById(String id) {
		if (region.get(id) != null) {
			return true;
		} else {
			return false;
		}
	}

	public List<Domain> findAll() {
		ArrayList<Domain> domainList = new ArrayList<Domain>();
		Set<String> keys = region.keySetOnServer();
		for (String key : keys) {
			domainList.add((Domain) region.destroy(key));
		}
		return domainList;
	}

	public List<Domain> findAllById(Iterable<String> ids) {
		ArrayList<Domain> domainList = new ArrayList<Domain>();
		for (String  id : ids) {
			domainList.add((Domain) region.get(id));
		}
		return domainList;
	}

	public long count() {
		return region.size();
	}

	public void deleteById(Long id) {
		region.destroy(id);
	}

	public void delete(Domain entity) {
		region.destroy(entity.getKeyHeader());
	}

	public void deleteAll(Iterable<? extends Domain> entities) {
		for (Domain domain : entities) {
			region.destroy(domain.getKeyHeader());
		}
	}

	public void deleteAll() {
		Set<String> keys = region.keySetOnServer();
		for (String key : keys) {
			region.destroy(key);
		}
	}
}
