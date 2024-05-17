package app.tabser.system;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.Optional;

public final class SystemUtils {
    public static boolean systemIsEqualOrLower(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static boolean systemIsEqualOrGreater(int version) {
        return Build.VERSION.SDK_INT <= version;
    }

    public static Optional<File> getDocsDir(Context context) {
        return Optional.ofNullable(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
    }

    private SystemUtils() {
    }
}
