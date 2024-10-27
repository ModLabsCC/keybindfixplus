package cc.modlabs.keybindfixplus

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import cc.modlabs.keybindfixplus.mixins.TimesPressedAccessor
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

object KeybindFixer {

    private val keyFixMap: Multimap<InputUtil.Key,KeyBinding> = ArrayListMultimap.create()

    fun putKey(key: InputUtil.Key, keyBinding: KeyBinding)
    {
        keyFixMap.put(key, keyBinding)
    }

    fun clearMap()
    {
        keyFixMap.clear()
    }

    fun onKeyPressed(key: InputUtil.Key, finalBinding: KeyBinding?, baseBinding: KeyBinding?)
    {
        if (finalBinding == null || baseBinding == null || finalBinding !== baseBinding) return
        for (theKey in keyFixMap[key])
        {
            if (theKey == null || theKey === baseBinding) continue
            (theKey as TimesPressedAccessor).timesPressed++
        }
    }

    fun setKeyPressed(key: InputUtil.Key, pressed: Boolean, finalBinding: KeyBinding?, baseBinding: KeyBinding?)
    {
        if (finalBinding == null || baseBinding == null || finalBinding !== baseBinding) return
        for (theKey in keyFixMap[key])
        {
            if (theKey == null || theKey === baseBinding) continue
            theKey.isPressed = pressed
        }
    }
}
