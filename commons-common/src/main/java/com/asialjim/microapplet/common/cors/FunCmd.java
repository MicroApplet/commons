package com.asialjim.microapplet.common.cors;

import java.util.function.Consumer;

/**
 * 功能命令
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface FunCmd<Res> extends Cmd<Res> {

    /**
     * 执行命令
     *
     * @since 2025/4/27
     */
    default void execute(Consumer<Res> consumer) {
        Res execute = execute();
        consumer.accept(execute);
    }
}