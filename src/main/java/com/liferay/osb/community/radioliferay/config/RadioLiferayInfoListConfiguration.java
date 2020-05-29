package com.liferay.osb.community.radioliferay.config;

import com.liferay.osb.community.radioliferay.homepage.RadioLiferayInfoListProviderHelper;

import aQute.bnd.annotation.metatype.Meta;
/**
 * Configuration for the RadioLiferay related InfoListProviders. Default values
 * taken from current (as of May 2020) community site liferay.dev - might need to
 * be adapted for anything where the userid and name for the filter doesn't match.
 * 
 * If the values don't match, the InfoListProviders will return empty Asset Lists.
 * 
 * @author Olaf Kock 
 * @see RadioLiferayInfoListProviderHelper
 */

@Meta.OCD(
	    id = "com.liferay.osb.community.radioliferay.config.RadioLiferayInfoListConfiguration"
	    , localization = "content/Language"
	    , name = "radio-liferay-info-list-provider-configuration-name"
	    , description = "radio-liferay-info-list-provider-configuration-description"
	)
public interface RadioLiferayInfoListConfiguration {
    
	@Meta.AD(
			deflt = "radioliferay",
			description = "radio-liferay-info-list-provider-filtered-tag-description",
			name = "radio-liferay-info-list-provider-filtered-tag-name",
			required = false
			)
	public String filteredTag();
	
	@Meta.AD(
            deflt = "Olaf Kock",
            description = "radio-liferay-info-list-provider-filtered-user-name-description",
            name = "radio-liferay-info-list-provider-filtered-user-name-name",
            required = false
        )
	public String filteredUserName();
	
	@Meta.AD(
			deflt = "1339768",
		    description = "radio-liferay-info-list-provider-filtered-user-id-description",
		    name = "radio-liferay-info-list-provider-filtered-user-id-name",
			required = false
			)
	public long filteredUserId();
   
}
