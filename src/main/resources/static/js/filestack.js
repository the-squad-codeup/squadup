// JS file to handle the filestack API service

import { Utils } from "./utils.js";

$(async function() {
    console.log("Inside filestack.js");

    const Fetch = {
        Get: {
            async filestackKey() {
                console.log("Inside get filestackKey. Key: ");
                let keys = await fetch(`${Utils.url()}keys`).then(res => res.json());
                console.log(keys);
                return keys.filestack_KEY;
            }
        },
        Post: {
            async profilePicture(file) {
                console.log("inside profilePicture. file: ");
                console.log(file);
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

    const FileStack = {
        async initialize() {
            console.log("inside initialize");
            this.client = await filestack.init(this.filestackKey);
            Events.initialize();
        },
        client: null,
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
                console.log(file);
                let uploadedPicture = await Fetch.Post.profilePicture(file, FileStack.csrfToken).then(res => res);
                console.log(uploadedPicture);
                $('.profile-image').css('background-image', `url("${uploadedPicture.url}")`)
            }
        },
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
                console.log(file);
                let uploadedPicture = await Fetch.Post.squadPicture(file, FileStack.csrfToken).then(res => res);
                console.log(uploadedPicture);
                $('.squad-image').attr('src', `${uploadedPicture.url}`);
            }
        },
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
                console.log(file);
                let uploadedPicture = await Fetch.Post.addSquadPicture(file, FileStack.csrfToken).then(res => res);
                console.log(uploadedPicture);
                $('.add-modal-squad-img').attr('src', `${uploadedPicture.url}`);
                $(".add-modal-squad-img").attr("data-squad-img-id", `${uploadedPicture.id}`);
            }
        },
        filestackKey: await Fetch.Get.filestackKey().then(res => res),
        csrfToken: $("meta[name='_csrf']").attr("content")
    };

    const Events = {
        initialize() {
            $(document)
                .on("click", "#upload-profile-picture", function() {
                    FileStack.client.picker(FileStack.userProfileOptions).open();
                })
                .on("click", ".squad-image", function() {
                    FileStack.client.picker(FileStack.squadOptions).open();
                })
                .on("click", ".add-modal-squad-img", function() {
                    FileStack.client.picker(FileStack.addSquadOptions).open();
                });
            ;
        }
    }

    FileStack.initialize();

});