function Stack() {
    this.dataStore = []
    this.top = 0;
    this.push = push
    this.pop = pop
    this.peek = peek
    this.length = length;
}

function push(element) {
    this.dataStore[this.top++] = element;
}

function peek() {
    return this.dataStore[this.top - 1];
}

function pop() {
    return this.dataStore[--this.top];
}

function clear() {
    this.top = 0
}

function length() {
    return this.top
}


function toJson(labels) {
    let sBuffer = '';
    let stack = new Stack()
    for (let i = 0; i < labels.length; i++) {
        let openLabel = /<[A-Za-z]+?>/ig
        let closeLabel = /<\/[A-Za-z]+?>/ig
        let selfOpeningAndClosing = /<[A-Za-z]+?\/>/ig
        if (selfOpeningAndClosing.test(labels[i])) {
            let name = labels[i].substr(1, labels[i].length - 3)
            let num = stack.length()
            let blanks = new Array(num + 2).join('  ')
            let blanks2 = new Array(num + 1).join('  ')
            sBuffer += '{\n' +
                blanks + '\"name\": \"' + name + '\"\n' +
                blanks2+ '}'
            if (i + 1 < labels.length && closeLabel.test(labels[i + 1])) {
                sBuffer +=  ']\n'
            } else {
                sBuffer += ','
            }
        } else if (openLabel.test(labels[i])) {
            let name = labels[i].substr(1, labels[i].length - 2)
            stack.push(name)
            let num = stack.length()
            let blanks = new Array(num + 1).join('  ')
            sBuffer += '{\n'
            sBuffer += blanks + '\"name\":\"' + name + '\",\n'
            sBuffer += blanks + '\"children\":['

        } else if (closeLabel.test(labels[i])) {
            stack.pop()
            let num = stack.length()
            let blanks = new Array(num + 1).join('  ')
            sBuffer += blanks + '}'
            if (stack.length() === 0) {
                sBuffer += '\n'
            } else {
                let closeLabel1 = /<\/[A-Za-z]+?>/ig
                if (i + 1 < labels.length && closeLabel1.test(labels[i + 1])) {
                    sBuffer +=  ']\n'
                } else {
                    sBuffer += ','
                }
            }
        } else {
            // never occur
        }
    }
    return sBuffer
}


let str = '<A>' +
    '  <B>' +
    '    <C/>' +
    '    <D/>' +
    '    <E/>' +
    '  </B>' +
    '  <F>' +
    '    <G/>' +
    '    <H/>' +
    '    <I/>' +
    '  </F>' +
    '</A>'
let labels = str.match(/<[\W\w]+?>/ig);
console.log(labels)
console.log(toJson(labels))