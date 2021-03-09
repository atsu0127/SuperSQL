Summary:   SuperSQL
Name:      SuperSQL
Version:   1.0
Release:   1
License:   Public
Group:     Applications
Source0:    %{name}-%{version}.tar.gz
Prefix:    /
BuildArch: noarch

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
install -m 0755 lib/* $RPM_BUILD_ROOT/opt/%{name}/lib
install -m 0755 bin/* $RPM_BUILD_ROOT/opt/%{name}/bin
install -m 0644 resource/* $RPM_BUILD_ROOT/opt/%{name}/result
install -m 0644 test_queries/* $RPM_BUILD_ROOT/opt/%{name}/sample
install -m 0644 config/* $HOME

%check
export RPM_BUILD_ROOT
$RPM_BUILD_ROOT/opt/%{name}/bin/ssql -v

%clean
rm -rf %{buildroot}

%files
%defattr(755,root,root)
/opt/%{name}/bin/*
/opt/%{name}/lib/*
%defattr(644,root,root)
/opt/%{name}/sample/*
/opt/%{name}/result/*
