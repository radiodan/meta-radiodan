SUMMARY = "libspotify"
HOMEPAGE = "https://developer.spotify.com/technologies/libspotify/"
SECTION = "apps/multimedia"
LICENSE  = "commercial"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a7efe9489ebf22e7f43756283669839"

def map_spotify_arch(d):
    import re

    a = d.getVar('PACKAGE_ARCH', True)

    if   re.match('(armv7|cortexa)', a): return 'armv7'
    elif re.match('(armv6|arm1176jzfs)', a): return 'armv6'
    return a

SPOTIFY_ARCH = "${@map_spotify_arch(d)}"

# Spotify do not provide hard-float versions ...
python do_check_eabi () {
    eabisfx = d.getVar('ARMPKGSFX_EABI', True)
    if eabisfx == 'hf':
        bb.fatal ("%s is only available for soft float eabi" % (d.getVar('PN', True)))
}

addtask check_eabi before do_fetch

SRC_URI   = "https://developer.spotify.com/download/libspotify/libspotify-${PV}-Linux-${SPOTIFY_ARCH}-release.tar.gz"

SRCMD5_armv6 = "11c92c757ff316a46e64d518d6d45b05"
SRCSHA_armv6 = "4fb888eeb486578fa3a08e15f5aa2101632e60b56a068553d05d5d4ee0a080cc"

SRCMD5_armv7a = "eb7e98849b3bb6d364fa74034602afbf"
SRCSHA_armv7a = "ad27b6c5aee5382b66b39bfea3b1752076b7abcc445979ce25c1ec9d7ff3aeda"

SRC_URI[md5sum] := "${SRCMD5}"
SRC_URI[sha256sum] := "${SRCSHA}"

S = "${WORKDIR}/libspotify-${PV}-Linux-${SPOTIFY_ARCH}-release"

# This removes the --hash-style argument from LDFLAGS, which in turn shuts up
# the sanity check for GNU_HASH
export LDFLAGS="${BUILD_LDFLAGS}"

# The binaries are already stripped
INHIBIT_PACKAGE_STRIP = "1"

inherit pkgconfig

do_compile () {
}

do_install () {
    install -d ${D}${includedir}/libspotify
    cp ${S}/include/libspotify/*.h ${D}${includedir}/libspotify

    install -d ${D}${libdir}
    cp -R ${S}/lib ${D}/usr

    install -d ${D}${datadir}
    cp -R ${S}/share ${D}/usr
}

FILES_${PN}-doc += "${datadir}/man3"

