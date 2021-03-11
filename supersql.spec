Summary:   supersql
Name:      supersql
Version:   1.0
Release:   1
License:   Public
Group:     Applications
Source0:    %{name}-%{version}.tar.gz
Prefix:    /
BuildArch: x86_64

%description

%prep
rm -rf %{buildroot}

%setup

%build

%pre

%install
install -d -m 0755 $RPM_BUILD_ROOT/opt/%{name}/lib
install -d -m 0755 $RPM_BUILD_ROOT/opt/%{name}/bin
install -d -m 0644 $RPM_BUILD_ROOT/opt/%{name}/sample
install -d -m 0644 $RPM_BUILD_ROOT/opt/%{name}/result
install -d -m 0644 $RPM_BUILD_ROOT/opt/%{name}/config
cp -r lib/* $RPM_BUILD_ROOT/opt/%{name}/lib
cp -r bin/* $RPM_BUILD_ROOT/opt/%{name}/bin
cp -r resource/* $RPM_BUILD_ROOT/opt/%{name}/result
cp -r test_queries/* $RPM_BUILD_ROOT/opt/%{name}/sample
cp -r config/* $RPM_BUILD_ROOT/opt/%{name}/config
chmod 755 -R $RPM_BUILD_ROOT/opt/%{name}/lib/*
chmod 755 -R $RPM_BUILD_ROOT/opt/%{name}/bin/*

%check
export RPM_BUILD_ROOT
$RPM_BUILD_ROOT/opt/%{name}/bin/ssql -v -c $RPM_BUILD_ROOT/opt/%{name}/config/config.ssql

%clean
rm -rf %{buildroot}

%files
%defattr(755,root,root,755)
/opt/%{name}/bin/*
/opt/%{name}/lib/*
%defattr(644,root,root,644)
/opt/%{name}/sample/*
/opt/%{name}/result/*
/opt/%{name}/config/*
