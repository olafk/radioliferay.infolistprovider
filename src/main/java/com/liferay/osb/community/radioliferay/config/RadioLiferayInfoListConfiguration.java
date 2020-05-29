package com.liferay.osb.community.radioliferay.config;

import aQute.bnd.annotation.metatype.Meta;

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
