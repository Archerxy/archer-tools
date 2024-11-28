package com.archer.tools.platform;

public final class Platform {
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;
    public static final int SOLARIS = 3;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static final int AIX = 7;
    public static final int ANDROID = 8;
    public static final int GNU = 9;
    public static final int KFREEBSD = 10;
    public static final int NETBSD = 11;

    public static final boolean RO_FIELDS;
    public static final boolean HAS_BUFFERS;
    public static final boolean HAS_AWT;
    public static final boolean HAS_JAWT;
    public static final String MATH_LIBRARY_NAME;
    public static final String C_LIBRARY_NAME;
    public static final boolean HAS_DLL_CALLBACKS;
    public static final String RESOURCE_PREFIX;

    private static final int osType;
    public static final String ARCH;

    static {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Linux")) {
            if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
                osType = ANDROID;
                System.setProperty("jna.nounpack", "true");
            }
            else {
                osType = LINUX;
            }
        }
        else if (osName.startsWith("AIX")) {
            osType = AIX;
        }
        else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            osType = MAC;
        }
        else if (osName.startsWith("Windows CE")) {
            osType = WINDOWSCE;
        }
        else if (osName.startsWith("Windows")) {
            osType = WINDOWS;
        }
        else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            osType = SOLARIS;
        }
        else if (osName.startsWith("FreeBSD")) {
            osType = FREEBSD;
        }
        else if (osName.startsWith("OpenBSD")) {
            osType = OPENBSD;
        }
        else if (osName.equalsIgnoreCase("gnu")) {
            osType = GNU;
        }
        else if (osName.equalsIgnoreCase("gnu/kfreebsd")) {
            osType = KFREEBSD;
        }
        else if (osName.equalsIgnoreCase("netbsd")) {
            osType = NETBSD;
        }
        else {
            osType = UNSPECIFIED;
        }
        boolean hasBuffers = false;
        try {
            Class.forName("java.nio.Buffer");
            hasBuffers = true;
        } catch(ClassNotFoundException ignore) {}
        HAS_AWT = osType != WINDOWSCE && osType != ANDROID && osType != AIX;
        HAS_JAWT = HAS_AWT && osType != MAC;
        HAS_BUFFERS = hasBuffers;
        RO_FIELDS = osType != WINDOWSCE;
        C_LIBRARY_NAME = osType == WINDOWS ? "msvcrt" : osType == WINDOWSCE ? "coredll" : "c";
        MATH_LIBRARY_NAME = osType == WINDOWS ? "msvcrt" : osType == WINDOWSCE ? "coredll" : "m";
        ARCH = getCanonicalArchitecture(System.getProperty("os.arch"), osType);
        HAS_DLL_CALLBACKS = osType == WINDOWS && !ARCH.startsWith("aarch");
        RESOURCE_PREFIX = getNativeLibraryResourcePrefix();
    }
    private Platform() { }
    public static final int getOSType() {
        return osType;
    }
    public static final boolean isMac() {
        return osType == MAC;
    }
    public static final boolean isAndroid() {
        return osType == ANDROID;
    }
    public static final boolean isLinux() {
        return osType == LINUX;
    }
    public static final boolean isAIX() {
        return osType == AIX;
    }
    public static final boolean isWindowsCE() {
        return osType == WINDOWSCE;
    }
    public static final boolean isWindows() {
        return osType == WINDOWS || osType == WINDOWSCE;
    }
    public static final boolean isSolaris() {
        return osType == SOLARIS;
    }
    public static final boolean isFreeBSD() {
        return osType == FREEBSD;
    }
    public static final boolean isOpenBSD() {
        return osType == OPENBSD;
    }
    public static final boolean isNetBSD() {
        return osType == NETBSD;
    }
    public static final boolean isGNU() {
        return osType == GNU;
    }
    public static final boolean iskFreeBSD() {
        return osType == KFREEBSD;
    }
    public static final boolean isX11() {
        return !Platform.isWindows() && !Platform.isMac();
    }
    public static final boolean hasRuntimeExec() {
        if (isWindowsCE() && "J9".equals(System.getProperty("java.vm.name")))
            return false;
        return true;
    }
    public static final boolean is64Bit() {
        String model = System.getProperty("sun.arch.data.model",
                                          System.getProperty("com.ibm.vm.bitmode"));
        if (model != null) {
            return "64".equals(model);
        }
        if ("x86-64".equals(ARCH)
            || "ia64".equals(ARCH)
            || "ppc64".equals(ARCH) || "ppc64le".equals(ARCH)
            || "sparcv9".equals(ARCH)
            || "mips64".equals(ARCH) || "mips64el".equals(ARCH)
            || "loongarch64".equals(ARCH)
            || "amd64".equals(ARCH)
            || "aarch64".equals(ARCH)) {
            return true;
        }
        return false;
    }

    public static final boolean isIntel() {
        if (ARCH.startsWith("x86")) {
            return true;
        }
        return false;
    }

    public static final boolean isPPC() {
        if (ARCH.startsWith("ppc")) {
            return true;
        }
        return false;
    }

    public static final boolean isARM() {
        return ARCH.startsWith("arm") || ARCH.startsWith("aarch");
    }

    public static final boolean isSPARC() {
        return ARCH.startsWith("sparc");
    }

    public static final boolean isMIPS() {
        if (ARCH.equals("mips")
            || ARCH.equals("mips64")
            || ARCH.equals("mipsel")
            || ARCH.equals("mips64el")) {
            return true;
        }
        return false;
    }

    public static final boolean isLoongArch() {
        return ARCH.startsWith("loongarch");
    }

    static String getCanonicalArchitecture(String arch, int platform) {
        arch = arch.toLowerCase().trim();
        if ("powerpc".equals(arch)) {
            arch = "ppc";
        }
        else if ("powerpc64".equals(arch)) {
            arch = "ppc64";
        }
        else if ("i386".equals(arch) || "i686".equals(arch)) {
            arch = "x86";
        }
        else if ("x86_64".equals(arch) || "amd64".equals(arch)) {
            arch = "x86-64";
        }
        else if ("zarch_64".equals(arch)) {
            arch = "s390x";
        }
        if ("ppc64".equals(arch) && "little".equals(System.getProperty("sun.cpu.endian"))) {
            arch = "ppc64le";
        }
        return arch;
    }

    static String getNativeLibraryResourcePrefix() {
        String prefix = System.getProperty("jna.prefix");
        if(prefix != null) {
            return prefix;
        } else {
            return getNativeLibraryResourcePrefix(getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
        }
    }

    static String getNativeLibraryResourcePrefix(int osType, String arch, String name) {
        String osPrefix;
        arch = getCanonicalArchitecture(arch, osType);
        switch(osType) {
            case Platform.ANDROID:
                if (arch.startsWith("arm")) {
                    arch = "arm";
                }
                osPrefix = "android-" + arch;
                break;
            case Platform.WINDOWS:
                osPrefix = "win32-" + arch;
                break;
            case Platform.WINDOWSCE:
                osPrefix = "w32ce-" + arch;
                break;
            case Platform.MAC:
                osPrefix = "darwin-" + arch;
                break;
            case Platform.LINUX:
                osPrefix = "linux-" + arch;
                break;
            case Platform.SOLARIS:
                osPrefix = "sunos-" + arch;
                break;
            case Platform.FREEBSD:
                osPrefix = "freebsd-" + arch;
                break;
            case Platform.OPENBSD:
                osPrefix = "openbsd-" + arch;
                break;
            case Platform.NETBSD:
                osPrefix = "netbsd-" + arch;
                break;
            case Platform.KFREEBSD:
                osPrefix = "kfreebsd-" + arch;
                break;
            default:
                osPrefix = name.toLowerCase();
                int space = osPrefix.indexOf(" ");
                if (space != -1) {
                    osPrefix = osPrefix.substring(0, space);
                }
                osPrefix += "-" + arch;
                break;
        }
        return osPrefix;
    }
}
