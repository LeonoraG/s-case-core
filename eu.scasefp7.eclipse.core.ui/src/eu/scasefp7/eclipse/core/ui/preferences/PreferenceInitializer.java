package eu.scasefp7.eclipse.core.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import eu.scasefp7.eclipse.core.ui.Activator;
import eu.scasefp7.eclipse.core.ui.ScaseUiConstants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	    store.setDefault(PreferenceConstants.P_PROJECT_DOMAIN, ScaseUiConstants.PROP_PROJECT_DOMAIN_DEFAULT);
		store.setDefault(PreferenceConstants.P_USE_PROJECT_PREFS, false);
		store.setDefault(PreferenceConstants.P_NLP_ENDPOINT, "http://nlp.scasefp7.eu:8010/"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_UML_ENDPOINT, "http://uml.scasefp7.com/"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_ONTOREPO_ENDPOINT, "http://109.231.126.165:8080"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_WSC_ENDPOINT, "sftp://109.231.127.61:22"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_CONTROLTOWER_ENDPOINT, "http://app.scasefp7.com:3000/"); //$NON-NLS-1$
	}
}
