PACKAGE_NAME := $(shell grep '^Name' $(CURDIR)/supersql.spec | tr -d "[:blank:]" | cut -f 2 -d':')
PACKAGE_VERSION := $(shell grep '^Version' $(CURDIR)/supersql.spec | tr -d "[:blank:]" | cut -f 2 -d':')
PACKAGE_RELEASE := $(shell grep '^Release' $(CURDIR)/supersql.spec | tr -d "[:blank:]" | cut -f 2 -d':')
PACKAGE_ARCH := $(shell grep '^BuildArch' $(CURDIR)/supersql.spec | tr -d "[:blank:]" | cut -f 2 -d':')
BUILDDIR = $(CURDIR)/rpmbuild
DESTDIR=

.PHONY: clean

create-rpm: tarball rpm

create-deb: create-rpm rpm2deb

check-bin: tarball rpm-test

rpm2deb:
    alien -d $(BUILDDIR)/RPMS/$(PACKAGE_ARCH)/$(PACKAGE_NAME)-$(PACKAGE_VERSION)-$(PACKAGE_RELEASE).$(PACKAGE_ARCH).rpm

tarball:
	@echo "Creating tarball"
	mkdir -p $(BUILDDIR)/SOURCES $(BUILDDIR)/BUILD $(BUILDDIR)/SRPMS $(BUILDDIR)/RPMS $(BUILDDIR)/SRPMS/$(PACKAGE_ARCH) $(BUILDDIR)/RPMS/$(PACKAGE_ARCH)
	./mvnw clean package
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
