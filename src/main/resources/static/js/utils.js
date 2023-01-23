export const Utils = {
    // Returns url path of current protocol and host so development and live will have fetch request work correctly
    url() {
        return `${window.location.protocol}//${window.location.host}/`;
    },
    // returns new date value of timestamp string passed in
    dateStringToJSDate(string) {
        let year = string.slice(0, 4);
        let month = string.slice(5, 7);
        let day = string.slice(8, 10);
        let hour = string.slice(11, 13);
        let minute = string.slice(14, 16);
        let second = string.slice(17, 19);
        let millisecond = string.slice(20, 23);
        let date = new Date(year, month - 1, day, hour, minute, second, millisecond);
        return date;
    }
};