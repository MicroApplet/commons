package com.asialjim.microapplet.common.cors;

/**
 * 命令
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface Cmd<Res> {

    /**
     * 执行命令
     * @return {@link Res }
     * @since 2025/4/27
     */
    Res execute();
}