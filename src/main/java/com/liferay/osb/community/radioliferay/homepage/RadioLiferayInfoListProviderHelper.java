package com.liferay.osb.community.radioliferay.homepage;

import com.liferay.asset.kernel.exception.NoSuchTagException;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.info.list.provider.InfoListProviderContext;
import com.liferay.osb.community.radioliferay.config.RadioLiferayInfoListConfiguration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Helper class to deduplicate the code for the two InfoListProviders
 * in this module.
 * Criterion for filtered posts:
 * * Blog articles
 * * Tagged "radioliferay"
 * * Written by "Olaf Kock"
 * 
 * Due to the available AssetEntryQuery, we are going the easiest route
 * here: Posts are filtered by these values, then checked on a matching
 * userId to harden this module against improbably attacks, where some
 * author fakes those articles by changing their name and posting tagged
 * blog articles. 
 * 
 * Probably nobody knows how to do this anyway, but it's better to be 
 * safe than sorry.
 * 
 * The implementation could be improved by using a proper DynamicQuery,
 * but due to the many different sources and the strictly limited number
 * of results, the current implementation is not considered too much of
 * a burden.
 * 
 * @author Olaf Kock 
 */


public class RadioLiferayInfoListProviderHelper {
	
	public static List<AssetEntry> getInfoList(
			RadioLiferayInfoListConfiguration config,
			AssetTagLocalService atls,
			AssetEntryLocalService aels,
			InfoListProviderContext infoListProviderContext) {

		String tagName = config.filteredTag();
		try {
			AssetTag assetTag = atls.getTag(infoListProviderContext.getGroupOptional().get().getGroupId(), tagName);
	
			AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
			long[] tagIds = { assetTag.getTagId() };

			assetEntryQuery.setUserName(config.filteredUserName());
			assetEntryQuery.setOrderByCol1("publishDate");
			assetEntryQuery.setAllTagIds(tagIds);
			assetEntryQuery.setClassName(BlogsEntry.class.getName());
			
			List<AssetEntry> assetEntryList = aels.getEntries(assetEntryQuery);
			List<AssetEntry> filteredList = new ArrayList<AssetEntry>(assetEntryList.size());
			
			// secondary filter, as user name may be spoofed
			for (Iterator<AssetEntry> iterator = assetEntryList.iterator(); iterator.hasNext();) {
				AssetEntry assetEntry = iterator.next();
				if(assetEntry.getUserId() == config.filteredUserId())
					filteredList.add(assetEntry);
			}
			if(log.isWarnEnabled() && assetEntryList.size() != filteredList.size()) {
				log.warn((assetEntryList.size()-filteredList.size()) + 
						" entries filtered by User-Ids. Is some author masking as fake '" + 
						config.filteredUserName() + "'?");
			}
			return filteredList;
			
		} catch (NoSuchTagException e) {
			log.error("Tag " + config.filteredTag() + " could not be found. " + e.getMessage());
		} catch (PortalException e) {
			log.error(e);
		}
	
		return new LinkedList<AssetEntry>();
	}

	public static final Log log = LogFactoryUtil.getLog(RadioLiferayInfoListProviderHelper.class); 

}
