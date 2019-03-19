package com.liferay.kris.model.listener.resourcepermission;

import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author kpatefield
 */
@Component(
	immediate = true,
	property = {
		// TODO enter required service properties
	},
	service = ModelListener.class
)
public class ResourcePermissionModelListener extends BaseModelListener<ResourcePermission> {

	@Activate
	public void activate(BundleContext context) {
		String location = context.getBundle().getLocation();
		_log.info("bundle " + location + " activated!!!");
	}

	@Override
	public void onAfterCreate(ResourcePermission model) throws ModelListenerException {
		/*
			Condition will enure you only pick ResourcePermission objects related to JournalArticle and 
			the owner		
		 */
		if (model.getName() == JournalArticle.class.getName() && model.getOwnerId() != 0) {
			
			/*
				The bitwizeValue is the sum of values for JournalArticle, you can look the values up in
				the resourceaction table 
			*/
			long bitwizeValue = 65;
			model.setActionIds(bitwizeValue);
			ResourcePermissionLocalServiceUtil.updateResourcePermission(model);
			/*
			Log you changed permissions on JournalArticle programatically
			 */
			_log.info("JournalArticle permissions changed by Model Listner - ResourcePermissionId ="  + model.getResourcePermissionId());
			
		}
	
		super.onAfterCreate(model);
	}
	
	private static final Log _log = LogFactoryUtil.getLog(ResourcePermissionModelListener.class);
	
}