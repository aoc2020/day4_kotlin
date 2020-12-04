@file:JvmName("Main")

package com.github.aoc2020.day2_kotlin

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

val mandatory = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

fun main() {
    val input = readFile("input.txt")
    val passportStringList = splitPassports(input);
    val passports = toSets(passportStringList);
    val validCount = passports.stream().filter { p -> isValid(p) }.count();
    println("Valids: $validCount");
    val valids = passports.stream().filter { p -> isValid(p) };
    var validCount2 = valids.filter { p -> isValid2(p) }.count();
    println("Valids 2: $validCount2");
}


fun isValid2(pass: Map<String, String>): Boolean {
    // byr (Birth Year) - four digits; at least 1920 and at most 2002.
//    iyr (Issue Year) - four digits; at least 2010 and at most 2020.
//    eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
    val byr = pass.getOrDefault("byr", "");
    if (!byr.matches("""^(19[2-9][0-9]|200[0-2])$""".toRegex())) return logAndFalse("byr", byr);
    val iyr = pass.getOrDefault("iyr", "");
    if (!iyr.matches("""^20(1[0-9]|20)$""".toRegex())) return logAndFalse("iyr", iyr);
    var eyr = pass.getOrDefault("eyr", "");
    if (!eyr.matches("""^20(2[0-9]|30)$""".toRegex())) return logAndFalse("eyr", eyr);
//    hgt (Height) - a number followed by either cm or in:
    var hgt = pass.getOrDefault("hgt", "");
    if (!hgt.matches("""^(1([5-8][0-9]|9[0-3])cm|(59|6[0-9]|7[0-6])in)$""".toRegex())) return logAndFalse("hgt", hgt);
//    If cm, the number must be at least 150 and at most 193.
//    If in, the number must be at least 59 and at most 76.
//    hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
    var hcl = pass.getOrDefault("hcl", "");
    if (!hcl.matches("""^#[0-9a-f]{6}$""".toRegex())) return logAndFalse("hcl", hcl);
//    ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
    var ecl = pass.getOrDefault("ecl", "");
    if (!ecl.matches("""^(amb|blu|brn|gry|grn|oth|hzl)""".toRegex())) return logAndFalse("ecl", ecl);
//    pid (Passport ID) - a nine-digit number, including leading zeroes.
    var pid = pass.getOrDefault("pid", "");
    if (!pid.matches("""^[0-9]{9}$""".toRegex())) return logAndFalse("pid", pid);
//    cid (Country ID) - ignored, missing or not.
    return true;
}

fun logAndFalse(k: String, v: String): Boolean {
    println("Invalid: $k : $v");
    return false;
}

fun isValid(pass: Map<String, String>): Boolean {
    // var mandatory = listOf("byr","iyr","eyr","hgt"."hcl","ecl","pid");
    // cid (Country ID)
    val valid = mandatory.stream().map { p -> pass.containsKey(p) }.allMatch { s -> s };
    // println("valid:$valid for $pass");
    return valid;
}

fun toMap(s: String): Map<String, String> {
    return s.split(" ").stream()
        .map { s -> s.trim() }
        .filter { s -> !"".equals(s) }
        .map { s -> s.split(":") }
        .collect(Collectors.toMap({ a -> a[0] }, { a -> a[1] }))
}

fun toSets(passList: List<String>): List<Map<String, String>> {
    return passList.stream().map { s -> toMap(s) }.collect((Collectors.toList()))
}

fun splitPassports(lines: List<String>): List<String> {
    var result = ArrayList<String>();
    var current = StringBuilder();
    for (s in lines) {
        if ("".equals(s)) {
            result.add(current.toString());
            current = StringBuilder();
        } else {
            current.append(' ').append(s);
        }
    }
    result.add(current.toString())
    return result;
}

fun readFile(fileName: String): List<String> {
    return Files.lines(Path.of(fileName))
        .collect(Collectors.toUnmodifiableList())
}