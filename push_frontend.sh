#!/bin/bash

src=src/main/webapp/WEB-INF
dest=target/brooklyn-1.0-SNAPSHOT/WEB-INF

cp {$src,$dest}/resources/js/FORGE.js
cp {$src,$dest}/resources/js/TROVE.js
cp {$src,$dest}/resources/js/three.js
cp {$src,$dest}/resources/js/priceworker.js
cp {$src,$dest}/views/fragments/staticHeader.jsp
cp {$src,$dest}/views/fragments/phonboardJS.jsp
cp {$src,$dest}/views/fragments/onboardJS.jsp
cp {$src,$dest}/resources/stylesheets/customizer.css
