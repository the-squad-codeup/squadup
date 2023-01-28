// JS file to handle the filestack API service

import { Utils } from "./utils.js";

$(async function() {

    // Fetch object with Get and Post methods
    const Fetch = {
        // Get methods
        Get: {
            // Method to access filestack key from backend
            async filestackKey() {
                let keys = await fetch(`${Utils.url()}keys`).then(res => res.json());
                return keys.filestack_KEY;
            }
        },
        // Post methods
        Post: {
            // Method to post profile picture to database
            async profilePicture(file) {
                let postOptions = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': FileStack.csrfToken
                    },
                    body: JSON.stringify(file)
                }
                return await fetch(`${Utils.url()}user/picture`, postOptions).then(res => res.json());
            },
            // Method to post squad picture to database
            async squadPicture(file) {
                let postOptions = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': FileStack.csrfToken
                    },
                    body: JSON.stringify(file)
                }
                return await fetch(`${Utils.url()}squads/${$("#modal-squad-info").attr("data-squad-id")}/picture`, postOptions).then(res => res.json());
            },
            // Method to add default squad picture to database
            async addSquadPicture(file) {
                let postOptions = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': FileStack.csrfToken
                    },
                    body: JSON.stringify(file)
                }
                return await fetch(`${Utils.url()}squads/picture/new`, postOptions).then(res => res.json());
            }
        }
    }

    // Filestack object and globals/methods
    const FileStack = {
        // initializes filestack api object and event handlers
        async initialize() {
            this.client = await filestack.init(this.filestackKey);
            Events.initialize();
        },
        client: null,
        // filestack options to use for user profile picture
        userProfileOptions: {
            fromSources: ["local_file_system", "url"],
            accept: ["image/*"],
            transformations: {
                crop: false,
                circle: true,
                rotate: false,
                force: true
            },
            imageMax: [480, 480],
            onFileUploadFinished: async function(file) {
                let uploadedPicture = await Fetch.Post.profilePicture(file, FileStack.csrfToken).then(res => res);
                $('.profile-image').css('background-image', `url("${uploadedPicture.url}")`)
            }
        },
        // filestack options to use for squad picture
        squadOptions: {
            fromSources: ["local_file_system", "url"],
            accept: ["image/*"],
            transformations: {
                crop: false,
                circle: true,
                rotate: false,
                force: true
            },
            imageMax: [480, 480],
            onFileUploadFinished: async function(file) {
                let uploadedPicture = await Fetch.Post.squadPicture(file, FileStack.csrfToken).then(res => res);
                $('.squad-image').attr('src', `${uploadedPicture.url}`);
                $(`.solo-squad[data-squad-id='${$("#modal-squad-info").attr("data-squad-id")}']`).find("img").attr('src', `${uploadedPicture.url}`);
            }
        },
        // filestack options to use for squad picture in add squad modal
        addSquadOptions: {
            fromSources: ["local_file_system", "url"],
            accept: ["image/*"],
            transformations: {
                crop: false,
                circle: true,
                rotate: false,
                force: true
            },
            imageMax: [480, 480],
            onFileUploadFinished: async function(file) {
                let uploadedPicture = await Fetch.Post.addSquadPicture(file, FileStack.csrfToken).then(res => res);
                $('.add-modal-squad-img').attr('src', `${uploadedPicture.url}`);
                $(".add-modal-squad-img").attr("data-squad-img-id", `${uploadedPicture.id}`);
            }
        },
        filestackKey: await Fetch.Get.filestackKey().then(res => res),
        csrfToken: $("meta[name='_csrf']").attr("content")
    };

    // Event handlers
    const Events = {
        initialize() {
            $(document)
                .on("click", "#upload-profile-picture", function() {
                    FileStack.client.picker(FileStack.userProfileOptions).open();
                })
                .on("click", ".squad-image", function() {
                    if($("#user-details-div").attr("data-is-owner") === "true" || $(".squad-image").hasClass("clickable")) {
                        FileStack.client.picker(FileStack.squadOptions).open();
                    }
                })
                .on("click", ".add-modal-squad-img", function() {
                    FileStack.client.picker(FileStack.addSquadOptions).open();
                });
            ;
        }
    }

    // Runs on js file load to initialize filestack object and event handlers
    FileStack.initialize();

});