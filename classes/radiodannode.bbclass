inherit radiodanapp

DEPENDS += "nodejs-native"
RDEPENDS_${PN} += "nodejs nodejs-npm"

# Borrowed from the nodejs recipes
def map_nodejs_arch(a, d):
    import re

    if   re.match('p(pc|owerpc)(|64)', a): return 'ppc'
    elif re.match('i.86$', a): return 'ia32'
    elif re.match('x86_64$', a): return 'x64'
    elif re.match('arm64$', a): return 'arm'
    return a

do_compile () {
    set -e

    export HTTP_PROXY=${HTTP_PROXY}
    export HTTPS_PROXY=${HTTPS_PROXY}
    export SOCKS_PROXY=${SOCKS_PROXY}
    export GIT_PROXY_COMMAND=${GIT_PROXY_COMMAND}

    # npm creates a cache in $HOME/.npm, so point $HOME at the WORKDIR
    export HOME="${WORKDIR}"

    cd ${S}
    npm install --arch=${@map_nodejs_arch(d.getVar('TARGET_ARCH', True), d)}
}

do_install_append() {
    cp -R ${S}/* ${D}${APPDIR}
}

FILES_${PN} = "${APPDIR} ${APPBASEDIR}/current"
FILES_${PN}-dbg += "${APPDIR}/node_modules/*/*/*/.debug \
                    ${APPDIR}/node_modules/*/*/*/*/.debug \
                    ${APPDIR}/node_modules/*/*/*/*/*/.debug \
                    ${APPDIR}/node_modules/*/*/*/*/*/*/.debug \
                    ${APPDIR}/node_modules/*/*/*/*/*/*/*/.debug \
                    ${APPDIR}/node_modules/*/*/*/*/*/*/*/*/.debug \
                   "
FILES_${PN}-staticdev += "${APPDIR}/node_modules/*/*/*.a"
