package io.github.mufca.libgdx.constant;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontConstants {

    private static final String EXTENDED_CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "abcdefghijklmnopqrstuvwxyz" +
        "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞ" +
        "àáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ" +
        "ĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚě" +
        "ĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳ" +
        "ĴĵĶķĸĹĺĻļĽľŁłŃńŅņŇňŉŊŋ" +
        "ŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠš" +
        "ŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽž" +
        "0123456789" +
        ".,:;!?\"'()[]{}<>«»„“”" +
        "@#$%^&*-+=_/\\|~`…" +
        "©®™€£¥¢₱₤";

    public static FreeTypeFontGenerator.FreeTypeFontParameter getDefaultFontParameters() {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        parameter.characters = EXTENDED_CHARACTER_SET;
        return parameter;
    }
}
