USE squadup_db;

# TRUNCATE locations;
INSERT INTO locations (timezone)
    VALUES ('Unspecified Time Zone'),
           ('Hawaii-Aleutian Time Zone'),
           ('Alaskan Time Zone'),
           ('Pacific Time Zone'),
           ('Mountain Time Zone'),
           ('Mountain Arizona Time Zone'),
           ('Central Time Zone'),
           ('Eastern Time Zone'),
           ('Atlantic Time Zone');

#           ('Eastern Time Zone'),
#            ('Central Time Zone'),
#            ('Mountain Time Zone'),
#            ('Pacific Time Zone'),
#            ('Alaska Time Zone'),
#            ('Hawaii-Aleutian Time Zone');

# TRUNCATE languages;
INSERT INTO languages (language)
    VALUES ('English'),
           ('Spanish'),
           ('Chinese'),
           ('Japanese'),
           ('Korean'),
           ('German'),
           ('French'),
           ('Italian'),
           ('Portuguese');

# TRUNCATE ratings;
INSERT INTO ratings (igdb_id, rating)
    VALUES (8, 'Everyone'),
           (9, 'Everyone 10 & up'),
           (10, 'Teen (13 & up)'),
           (11, 'Mature (17 & up)'),
           (12, 'Adults Only (18 & up)');

# TRUNCATE platforms;
INSERT INTO platforms (type)
    VALUES ('PC'),
           ('Playstation'),
           ('Xbox'),
           ('Nintendo');