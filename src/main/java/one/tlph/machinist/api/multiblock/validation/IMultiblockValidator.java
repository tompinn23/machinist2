package one.tlph.machinist.api.multiblock.validation;


/*
 * A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef
 * https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9 by ZeroNoRyouki
 * https://github.com/ZeroNoRyouki/ZeroCore
 *
 * Ported to Minecraft 1.12 by c0dd
 * https://gitlab.tlph.one/c0dd/machinist
 */

public interface IMultiblockValidator {

    /**
     * @return the last validation error encountered when trying to assemble the multiblock, or null if there is no error.
     */
    ValidationError getLastError();

    /**
     * Set a validation error
     * @param error the error
     */
    void setLastError(ValidationError error);

    /**
     * Set a validation error
     * @param messageFormatStringResourceKey a translation key for a message or a message format string
     * @param messageParameters optional parameters for a message format string
     */
    void setLastError(String messageFormatStringResourceKey, Object... messageParameters);
}
