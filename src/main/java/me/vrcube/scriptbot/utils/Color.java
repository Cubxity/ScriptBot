/*
 * ScriptBot, 100% Customizable discord bot bridge plugin.
 *     Copyright (C) 2017  VRCube
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.vrcube.scriptbot.utils;

import org.bukkit.ChatColor;

public class Color {
    public static String of(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
