install-flyway:
	wget https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/9.19.3/flyway-commandline-9.19.3-macosx-x64.tar.gz
	tar -xvzf flyway-commandline-9.19.3-macosx-x64.tar.gz

migrate-up:
	cp flyway.conf ./flyway-9.19.3/conf/flyway.conf && ./flyway-9.19.3/flyway migrate -outputType=json

migrate-down:
	cp flyway.conf ./flyway-9.19.3/conf/flyway.conf && ./flyway-9.19.3/flyway clean -outputType=json

run:
	gradle bootRun

local:
	make migrate-up run