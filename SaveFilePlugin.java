package cm.ecrivo.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.OutputStream;

/**
 * Enregistre un fichier dans le dossier Téléchargements/Ecrivo du téléphone,
 * visible dans le gestionnaire de fichiers (Android 10 et plus récent).
 */
@CapacitorPlugin(name = "SaveFile")
public class SaveFilePlugin extends Plugin {

    @PluginMethod
    public void save(PluginCall call) {
        String name = call.getString("name");
        String mime = call.getString("mime", "application/octet-stream");
        String data = call.getString("data");
        if (name == null || data == null) {
            call.reject("Paramètres manquants");
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            call.reject("Version d'Android trop ancienne pour l'enregistrement direct");
            return;
        }
        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            ContentResolver resolver = getContext().getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mime);
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Ecrivo");
            Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            if (uri == null) {
                call.reject("Création du fichier impossible");
                return;
            }
            OutputStream out = resolver.openOutputStream(uri);
            if (out == null) {
                call.reject("Ouverture du fichier impossible");
                return;
            }
            try {
                out.write(bytes);
            } finally {
                out.close();
            }
            JSObject ret = new JSObject();
            ret.put("where", "Téléchargements/Ecrivo");
            call.resolve(ret);
        } catch (Exception e) {
            call.reject(e.getMessage() == null ? "Erreur d'enregistrement" : e.getMessage());
        }
    }
}
