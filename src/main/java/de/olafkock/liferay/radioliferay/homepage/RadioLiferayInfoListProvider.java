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
 * An InfoListProvider that returns all Radio Liferay posts based on configured
 * criteria. 
 * 
 * @author Olaf Kock 
 * @see RadioLiferayInfoListProviderHelper
 */



@Component(
		service = InfoListProvider.class,
		configurationPid = "de.olafkock.liferay.radioliferay.config.RadioLiferayInfoListConfiguration"
)
public class RadioLiferayInfoListProvider implements InfoListProvider<AssetEntry>{

	@Override
	public List<AssetEntry> getInfoList(InfoListProviderContext infoListProviderContext) {
		return RadioLiferayInfoListProviderHelper.getInfoList(config, _assetTagLocalService, _assetEntryLocalService, infoListProviderContext);
	}

	@Override
	public List<AssetEntry> getInfoList(InfoListProviderContext infoListProviderContext, Pagination pagination,
			Sort sort) {
		List<AssetEntry> result = RadioLiferayInfoListProviderHelper.getInfoList(config, _assetTagLocalService, _assetEntryLocalService, infoListProviderContext);
		return result.subList(pagination.getStart(), pagination.getEnd());
    }

	@Override
	public int getInfoListCount(InfoListProviderContext infoListProviderContext) {
        return getInfoList(infoListProviderContext).size();
    }

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle("content.Language", locale, getClass());
		return LanguageUtil.get(resourceBundle, "radio-liferay-infolist-all-episodes");
	}
	
	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		config = ConfigurableUtil.createConfigurable(RadioLiferayInfoListConfiguration.class, properties);
	}
	
	private volatile RadioLiferayInfoListConfiguration config = null;

	@Reference
	AssetEntryLocalService _assetEntryLocalService;
	
	@Reference
	AssetTagLocalService _assetTagLocalService;
	
//	private static final Log log = LogFactoryUtil.getLog(RadioLiferayInfoListProvider.class); 
}
