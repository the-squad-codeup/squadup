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
                        'Content-Type' : 'application/json',
                        'X-CSRF-TOKEN' : FileStack.csrfToken
                    },
                    body: JSON.stringify(file)
                }
                return await fetch(`${Utils.url()}user/picture`, postOptions).then(res => res.json());
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
        options: {
            fromSources: ["local_file_system", "url"],
            accept: ["image/*"],
            transformations: {
                crop: false,
                circle: true,
                rotate: false,
                force: true
            },
            imageMax: [240, 240],
            onFileUploadFinished: async function(file) {
                console.log(file);
                let uploadedPicture = await Fetch.Post.profilePicture(file, FileStack.csrfToken).then(res => res);
                console.log(uploadedPicture);
                $("#pic-div").append(`
                    <img src="${uploadedPicture.url}">
                `);
                $("#navbar-profile-image").parent().empty().append(`
                    <img id="navbar-profile-image" src="${uploadedPicture.url}" style="max-height: 1.75em; max-width: 1.75em;">
                `);
            }
        },
        filestackKey: await Fetch.Get.filestackKey().then(res => res),
        csrfToken: $("meta[name='_csrf']").attr("content")
    };

    const Events = {
        initialize() {
            $(document).on("click", "#upload-profile-picture", function() {
                FileStack.client.picker(FileStack.options).open();
            });
        }
    }

    FileStack.initialize();

});