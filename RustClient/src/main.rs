use serde::{Deserialize, Serialize};
use std::io::Read;
use std::net::{Shutdown, TcpStream};
use std::str::from_utf8;

const PORT: i32 = 9999;
const HOST: &str = "localhost";

#[derive(Serialize, Deserialize, Debug)]
struct SocketMessage<'a> {
    content: &'a str,
    time: i64,
}

fn main() {
    match TcpStream::connect(format!("{}:{}", HOST, PORT)) {
        Ok(mut stream) => {
            println!("Successfully connected to {} in port {}", HOST, PORT);
            let mut buffer = [0 as u8; 512]; // 512 bytes buffer
            while match stream.read(&mut buffer) {
                Ok(_) => {
                    // Have to remove extra 0's from string
                    let content = from_utf8(&buffer).unwrap_or("")
                        .to_string().trim_end_matches("\0").to_string();
                    let message: SocketMessage = serde_json::from_str(&content).unwrap();
                    println!("Received: \"{}\", at time: {}", message.content, message.time);
                    buffer.iter_mut().for_each(|x| { *x = 0 }); // Clears buffer
                    true
                }
                Err(e) => {
                    println!("Failed to receive data: {}", e);
                    stream.shutdown(Shutdown::Both).unwrap();
                    false
                }
            } {}
        }
        Err(e) => {
            println!("Failed to connect: {}", e);
        }
    }
    println!("Terminated.");
}
