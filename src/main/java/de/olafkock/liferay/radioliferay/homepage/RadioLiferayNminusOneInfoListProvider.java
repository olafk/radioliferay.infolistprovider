package de.olafkock.liferay.radioliferay.homepage;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.info.list.provider.InfoListProvider;
import com.liferay.info.list.provider.InfoListProviderContext;
import com.liferay.info.pagination.Pagination;
import com.liferay.info.sort.Sort;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import de.olafkock.liferay.radioliferay.config.RadioLiferayInfoListConfiguration;

/**
 * An InfoListProvider that returns all but the newest Radio Liferay 
 * posts based on configured criteria.
 * 
 * The "...but the newest..." part is because Asset Publisher allows
 * to configure the number of posts, but not the starting position.
 * On the original homepage, we wanted to show the latest post in full
 * content, and all the other episodes as Abstract. An Application
 * Display Template was too much work - the pragmatic solution is
 * this provider.
 * 
 * @author Olaf Kock 
 * @see RadioLiferayInfoListProviderHelper
 */

@Component(
		service=InfoListProvider.class,
		configurationPid = "de.olafkock.liferay.radioliferay.config.RadioLiferayInfoListConfiguration"
)
public class RadioLiferayNminusOneInfoListProvider implements InfoListProvider<AssetEntry>{

	@Override
	public List<AssetEntry> getInfoList(InfoListProviderContext infoListProviderContext) {
		List<AssetEntry> result = RadioLiferayInfoListProviderHelper.getInfoList(config, _assetTagLocalService, _assetEntryLocalService, infoListProviderContext);
		if(!result.isEmpty()) {
			result.remove(0);
		}
		return result;
	}

	/**
	 * I could never find an occasion where this method is called. Simplest possible
	 * implementation, but might break if some index runs out of bounds (e.g. if pagination
	 * doesn't pay attention to size): I just don't know, couldn't test it.
	 */
	@Override
	public List<AssetEntry> getInfoList(InfoListProviderContext infoListProviderContext, Pagination pagination,
			Sort sort) {
		List<AssetEntry> result = getInfoList(infoListProviderContext);
		return result.subList(pagination.getStart(), pagination.getEnd());
    }

	@Override
	public int getInfoListCount(InfoListProviderContext infoListProviderContext) {
        return getInfoList(infoListProviderContext).size();
    }
	
	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle("content.Language", locale, getClass());
		return LanguageUtil.get(resourceBundle, "radio-liferay-infolist-all-but-the-latest-episode");
	}
	
	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		config = ConfigurableUtil.createConfigurable(RadioLiferayInfoListConfiguration.class, properties);
	}

	private RadioLiferayInfoListConfiguration config = null;

	@Reference
	AssetEntryLocalService _assetEntryLocalService;
	
	@Reference
	AssetTagLocalService _assetTagLocalService;
	
//	private static final Log log = LogFactoryUtil.getLog(RadioLiferayInfoListProvider.class); 
}
