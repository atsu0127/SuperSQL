PACKAGE_NAME := $(shell grep '^Name' $(CURDIR)/SuperSQL.spec | tr -d "[:blank:]" | cut -f 2 -d':')
PACKAGE_VERSION := $(shell grep '^Version' $(CURDIR)/SuperSQL.spec | tr -d "[:blank:]" | cut -f 2 -d':')
PACKAGE_ARCH := $(shell grep '^BuildArch' $(CURDIR)/SuperSQL.spec | tr -d "[:blank:]" | cut -f 2 -d':')
BUILDDIR = $(CURDIR)/rpmbuild
DESTDIR=

.PHONY: clean

create-package: tarball rpm

check-bin: tarball rpm-test

tarball:
	@echo "Creating tarball"
	mkdir -p $(BUILDDIR)/{SOURCES,BUILD,SRPMS,RPMS} $(BUILDDIR)/SRPMS/$(PACKAGE_ARCH) $(BUILDDIR)/RPMS/$(PACKAGE_ARCH)
	mvn clean package
	mv $(CURDIR)/target/$(PACKAGE_NAME)-$(PACKAGE_VERSION)-bin.tar.gz $(BUILDDIR)/SOURCES/$(PACKAGE_NAME)-$(PACKAGE_VERSION).tar.gz

rpm:
	@echo "Building RPM pakcage "
	sudo yum-builddep -q -y $(PACKAGE_NAME).spec
	rpmbuild \
		--define "_topdir $(BUILDDIR)" \
		-ba $(PACKAGE_NAME).spec

rpm-test:
	@echo "Building RPM pakcage "
	sudo yum-builddep -q -y $(PACKAGE_NAME).spec
	rpmbuild \
		--define "_topdir $(BUILDDIR)" \
		-bi $(PACKAGE_NAME).spec

clean:
	rm -rf $(BUILDDIR)