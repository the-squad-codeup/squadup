USE squadup_db;

TRUNCATE locations;
INSERT INTO locations (timezone)
    VALUES ('Eastern Time Zone'),
           ('Central Time Zone'),
           ('Mountain Time Zone'),
           ('Pacific Time Zone'),
           ('Alaska Time Zone'),
           ('Hawaii-Aleutian Time Zone');

TRUNCATE languages;
INSERT INTO languages (language)
    VALUES ('English'),
           ('Chinese'),
           ('Japanese'),
           ('Korean'),
           ('German'),
           ('French'),
           ('Italian'),
           ('Portuguese');

TRUNCATE ratings;
INSERT INTO ratings (rating)
    VALUES ('Everyone'),
           ('Everyone 10 & up'),
           ('Teen (13 & up)'),
           ('Mature (17 & up)'),
           ('Adults Only (18 & up)'),
           ('Rating Pending'),
           ('Rating Pending Likely Mature');

TRUNCATE platforms;
INSERT INTO platforms (type)
    VALUES ('PC'),
           ('Playstation'),
           ('Xbox'),
           ('Nintendo');