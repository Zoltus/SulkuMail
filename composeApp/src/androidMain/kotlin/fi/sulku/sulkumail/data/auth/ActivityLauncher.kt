package fi.sulku.sulkumail.data.auth

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

object ActivityHolder {
    private var activityRef: WeakReference<ComponentActivity>? = null
    private val launchers = mutableMapOf<String, ActivityResultLauncher<*>>()

    // Initialize with a specific ComponentActivity
    fun init(activity: ComponentActivity) {
        activityRef = WeakReference(activity)

        // Set up auto-cleanup when activity is destroyed
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (activityRef?.get() == activity) {
                    cleanup()
                }
                super.onDestroy(owner)
            }
        })
    }

    fun getActivity(): ComponentActivity? = activityRef?.get()

    // Clean up resources
    private fun cleanup() {
        activityRef = null
        launchers.forEach { (_, launcher) ->
            try { launcher.unregister() } catch (e: Exception) { /* Ignore */ }
        }
        launchers.clear()
    }

    fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I>? {
        val activity = getActivity() ?: return null
        val key = "key_${System.currentTimeMillis()}_${launchers.size}"

        val launcher = activity.activityResultRegistry.register(key, contract, callback)
        launchers[key] = launcher
        return launcher
    }
}